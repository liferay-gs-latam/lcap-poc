# General Scripting Language Client Extension

## Objetivo
Testar o conceito Low Code no Liferay através de scripting language aprovado via processo de workflow :nerd_face:
Basicamente vamos aprimorar o que hoje é feito com groovy

## Motivações
* O groovy atual não dá suporte para aprovação de workflow
* Não é permitido em SaaS
* Pode ser verboso, apesar de ser groovy

## Benefícios
* Facilidade na criação de regras de negócio permitindo lógica de programação, estrutura de dados e suporte a consumo de APIs
* Não é necessário criar novos client-extensions, lidar com deployments, etc
* Na criação do script não são necessários imports, apenas a implementação da lógica de forma. O script é executado em um sandbox com todas as importações necessárias.
* Os scripts são submetidos a um workflow e somente são executados se estiverem aprovados

## Como funciona
* Um client extension de uso geral é instalado, General Scripting Language Client Extension. Ele pode ser usado no evento AfterUpdate de qualquer Object Definition
* Um ObjectDefinition especial é criado no Liferay: Script. Esse object é responsável por conter lógicas de negócio escritas em scripting language e mapear para qual ObjectDefinition esse script se aplica

## Exemplo prático:
* O usuário cria um ObjectDefinition chamado Noticias
* Ele deseja que após o cadastro de uma noticia sejam feitas validações especiais, consumindo APIs externas e depois atribuindo tags customizadas a notícias de acordo com regras específicas
* O usuário associa o evento AfterUpdate da Noticia ao Client Extension
* Depois ele cria uma nova Entry no Object Script definindo o object Noticias como alvo, e um script a ser executado no sandbox do client extension
* No script o usuário só precisa manipular o JSON e escrever chamadas REST que forem necessárias. Por exemplo o código poderia se parecer com isso
```
if (inputObj["name"] == null || inputObj["age"] !is Int || inputObj["age"] < 18) {
    result = json.encodeToString(mapOf("error" to "Invalid input"))
} else {
    val client = HttpClient()
    try {
        val response: String = client.get("https://api.example.com/user/${inputObj["name"]}")
        val responseData = json.decodeFromString<Map<String, Any>>(response)
        result = json.encodeToString(mapOf("status" to 200, "data" to responseData))
    } catch (e: Exception) {
        result = json.encodeToString(mapOf("error" to e.message))
    } finally {
        client.close()
    }
}
```

Exemplo de controler com sandbox para execução de scipt kotlin
```
@RestController
@RequestMapping("/script")
class ScriptController {

    @PostMapping("/execute")
    fun executeScript(@RequestBody request: ScriptRequest): String {
        val engine = KotlinJsr223JvmLocalScriptEngineFactory().scriptEngine
        val bindings = SimpleBindings()

        // Pré-configuração do objeto inputObj com o JSON de entrada
        val inputObj = json.decodeFromString<Map<String, Any>>(request.inputData)

        // Passa o objeto inputObj para o script
        bindings["inputObj"] = inputObj

        // Configuração padrão de imports
        val defaultImports = """
            import io.ktor.client.*
            import io.ktor.client.request.*
            import kotlinx.coroutines.runBlocking
            import kotlinx.serialization.json.Json
        """

        // Executa o script com os imports configurados
        val script = "$defaultImports\n${request.script}"
        engine.eval(script, bindings)
        return bindings["result"] as String
    }
}

data class ScriptRequest(val script: String, val inputData: String)
```

A única coisa é que nesse exemplo o script chega por parametro. Na PoC não será assim.
* Vamos receber por parametro somente o ObjectEntry que desejamos manipular, ele chega como JSON.
* O script nós vamos consumir via headless API do object Script




