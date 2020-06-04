package challenge.codenation.model

data class Challenge(val numero_casas: Int? = null,
                     val token: String? = null,
                     val cifrado: String? = null,
                     var decifrado: String? = null,
                     var resumo_criptografico: String? = null)

data class ChallengeResult(val score: Int? = null)
