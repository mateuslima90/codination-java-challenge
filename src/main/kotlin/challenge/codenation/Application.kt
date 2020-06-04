package challenge.codenation

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("challenge.codenation")
                .mainClass(Application.javaClass)
                .start()
    }
}