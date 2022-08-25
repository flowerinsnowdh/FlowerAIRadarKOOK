package online.flowerinsnow.flowerairadar.kook.util

object IOUtils {
    fun closeQuietly(vararg acs : AutoCloseable?) {
        acs.forEach {
            if (it != null) {
                try {
                    it.close()
                } catch (_ : Exception) {
                }
            }
        }
    }
}