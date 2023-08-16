package modulos.produto;
import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API rest do modulo de produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void BeforeEach(){
        baseURI = "http://165.227.93.41";
        basePath = "/lojinha-bugada";
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
