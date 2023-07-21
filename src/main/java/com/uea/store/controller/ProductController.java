package com.uea.store.controller;

import com.uea.store.dto.ProductRequestDto;
import com.uea.store.dto.ProductResponseDto;
import com.uea.store.model.Category;
import com.uea.store.model.Product;
import com.uea.store.repository.CategoryRepository;
import com.uea.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Ao invés de retornar um Product diretamente, retorna o DTO para evitar que o atributo "category" retorne como objeto JSON.
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> list() {
        // Instancia a lista que será retornada.
        List<ProductResponseDto> productList = new ArrayList<>();


        // Para cada Product que existir, o converta para sua forma DTo e adione na lista.
        for (Product product : productRepository.findAll()) {
            ProductResponseDto productAsDto = ProductResponseDto.convert(product);

            productList.add(productAsDto);
        }

        // Retorna a lista no corpo do objeto ResponseEntity que cria a reposta da requisição http, com status de ok.
        return ResponseEntity.ok(productList);
    }

    // Processo similar para a rota de consulta do produto por ID.
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Integer id) {
        Optional<Product> product = productRepository.findById(id);

        // Se o produto for encontrado.
        if (product.isPresent()) {
            ProductResponseDto productAsDto = ProductResponseDto.convert(product.get());

            return ResponseEntity.ok(productAsDto);
        }

        return ResponseEntity.notFound().build();
    }

    // A rota de create retorna um Object para que o Java aceite dois tipos de retornos diferentes, já que para o caso de o a categoria não existir para aquele produto, será retornada apenas uma string informando isto.
    // Um outro ponto relevante é que esta rota espera um ProductRequestDto, este DTO apenas remove a necessidade do usuário passa o "id" para o produto, uma vez que este campo é gerado pelo banco de dados.
    @PostMapping
    ResponseEntity<Object> create(@RequestBody ProductRequestDto productRequestDto) {
        Optional<Category> newCategory = categoryRepository.findById(productRequestDto.getCategory());

        if (!newCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no category with that name!");
        }

        // Como dito, para a interação com o banco deve ser utilizado a classe que representa a entidade no banco de dados, por isto, criamos este objeto a partir das informações passadas no ProductRequestDto.
        Product _product = ProductRequestDto.convert(productRequestDto, newCategory.get());

        // Um detalhe: quando utilizarmos o objeto repository do JPA para salvar o Product no banco, será criado um novo objeto da classe Product, já com o id gerado pelo banco de dados, é interessante retornar esta informação na mesma requisição para o frontend.
        ProductResponseDto productResponse = ProductResponseDto.convert(productRepository.save(_product));

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    // A rota de update do produto recebe o id do produto a ser atualizado na rota, e no corpo recebe as novas propriedades do produto.
    @PutMapping("/{id}")
    ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody ProductRequestDto productRequestDto) {
        Optional<Product> productToUpdate = productRepository.findById(id);

        // Veririca se o produto com o id passada existe, caso não exista não será possível atualizar nada em banco, portanto já é retornado um notFound().
        if (!productToUpdate.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Jã verificamos que o produto existe, agora iremos verificar, assim como na rota de criação, que a categoria passada existe.
        Optional<Category> category = categoryRepository.findById(productRequestDto.getCategory());

        if (!category.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no category with that name!");
        }

        // Captura a referência daquele produto.
        Product sameProduct = productToUpdate.get();

        // Atualiza todos os seus campos conforme os valores passados no corpo da requisição.
        sameProduct.setName(productRequestDto.getName());
        sameProduct.setCategory(category.get());
        sameProduct.setDescription(productRequestDto.getDescription());
        sameProduct.setPurchaseDate(productRequestDto.getPurchaseDate());
        sameProduct.setPrice(productRequestDto.getPrice());

        // Novamente como a rota de create, salva esse objeto no banco e converte o retorno para o DTO de respostas, para que o tratamento do campo Category e retorno do campo "_id".
        ProductResponseDto productResponseDto = ProductResponseDto.convert(productRepository.save(sameProduct));

        return ResponseEntity.ok(productResponseDto);
    }

    // Na rota de delete não é necessário retornar nenhum corpo, apenas é interessante que sejam retornados os códigos HTTP corretos para as ações, como noContent() ou notFound(), 204 e 404, respectivamente.
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
