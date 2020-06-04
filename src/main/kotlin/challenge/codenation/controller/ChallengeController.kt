package challenge.codenation.controller

import challenge.codenation.model.Challenge
import challenge.codenation.model.ChallengeResult
import challenge.codenation.service.ChallengeService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.reactivex.Single

@Controller
class ChallengeController(val challengeService: ChallengeService) {

    @Get("/mychallenge/{token}")
    fun getMyChallenge(token: String): Single<Challenge> {
        return challengeService.getMyChallenge(token)
    }

    @Post("/submit/{token}")
    fun submitMyChallenge(token: String): Single<ChallengeResult?> {
        return challengeService.submitMyChallenge(token)
    }
}