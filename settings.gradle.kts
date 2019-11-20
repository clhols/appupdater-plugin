include(":")

buildCache {
    remote(HttpBuildCache::class.java) {
        url = uri("http://192.168.87.91:5071/cache/")
        isPush = System.getenv("CI") == "true"
        credentials {
            username = "youtec"
            password = "QapcErxW2pSa3pR"
        }
    }
}