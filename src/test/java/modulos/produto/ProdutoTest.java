package modulos.produto;

import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.ComponentePojo;
import pojo.ProdutoPojo;
import pojo.UsuarioPojo;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API rest do modulo de produto")

public class ProdutoTest {

    private String token;


    @BeforeEach
    public void BeforeEach(){
        //configurando os dados da API rest da lojinha
        baseURI = "http://165.227.93.41";
        //port
        basePath = "/lojinha-bugada";
        
        //obter o token do usuario admin
        this.token = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.criarUsuarioAdmistrador())
            .when()
                .post("/v2/login")
            .then()
                .extract()
                    .path("data.token");


    }


    @Test
    @DisplayName("Validar que o valor o produto igual 0.00 não é permitido")

    public void testValidarLimiteZeradoProibidoValorProduto(){

        //tentar inserir um produto com valor 0.00 e validar que a mensagem de erro foi apresentada e o
        //status code retornado foi 422

        given()
                .contentType(ContentType.JSON)
                .header("token",this.token)
                .body(ProdutoDataFactory.criarProdutoComunComOValorIgualA(0.00))


        .when()
                .post("/v2/produtos")
        .then()
                .assertThat()
                .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);
    }

    @Test
    @DisplayName("Validar que o valor do produto igual 7000.01 não é permitido")

    public void testValidarLimiteMaiorSeteMilProibidoValorProduto(){

        //tentar inserir um produto com valor 7000.1 e validar que a mensagem de erro foi apresentada e o
        //status code retornado foi 422

        given()
                .contentType(ContentType.JSON)
                .header("token",this.token)
                .body(ProdutoDataFactory.criarProdutoComunComOValorIgualA(7000.01))


            .when()
                .post("/v2/produtos")
            .then()
                .assertThat()
                    .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);
    }



}
