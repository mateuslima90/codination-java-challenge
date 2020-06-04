package challenge.codenation.service

import challenge.codenation.client.CodinationClient
import challenge.codenation.model.Challenge
import challenge.codenation.model.ChallengeResult
import io.reactivex.Flowable
import io.reactivex.Single
import java.security.MessageDigest
import javax.inject.Singleton

@Singleton
class ChallengeService(private val codinationClient: CodinationClient) {

    private val lettersList: List<String> = mutableListOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")

    fun getMyChallenge(token: String): Single<Challenge> {

        return codinationClient.getChallenge(token)
                .map { response -> transformChallenge(response)}
                .firstOrError()
    }

    fun submitMyChallenge(token: String): Single<ChallengeResult?> {

        return codinationClient.getChallenge(token)
                .map { response -> transformChallenge(response) }
                .flatMap { response -> codinationClient.sentChallenge(token, response)}
                .firstOrError()
    }

    private fun transformChallenge(challenge: Challenge): Challenge {

        challenge.decifrado = decryptCesarCrypt(challenge.cifrado!!)

        challenge.resumo_criptografico = hashString(challenge.decifrado!!)

        return challenge
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest
                .getInstance("SHA-1")
                .digest(input.toByteArray())
        val result = StringBuilder(bytes.size)

        bytes.forEach {
            val i = it.toInt()
            result.append(Integer.toString(i and 0xff + 0x100, 16)).substring(1)
        }

        return result.toString()
    }

    private fun decryptCesarCrypt(message: String) : String {

        val phrase = message.toList()

        var decryptMessage = ""

        phrase.forEach { letter ->
            decryptMessage = decryptMessage.plus(findOriginalLetter(letter.toString()))
        }
        println(decryptMessage)
        return decryptMessage
    }

    private fun findOriginalLetter(letter : String): String {

        val position = lettersList.indexOf(letter)

        val last = 26

        if(".".equals(letter) || ",".equals(letter) || " ".equals(letter))
            return letter

        if( position - 3 >= 0){
            return lettersList.get(position-3)
        } else {
            val result = position - 3
            return lettersList.get(last + result)
        }
    }

}