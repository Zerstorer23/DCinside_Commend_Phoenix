package lexington

import be.zvz.kotlininside.KotlinInside
import be.zvz.kotlininside.api.article.ArticleVote
import be.zvz.kotlininside.http.DefaultHttpClient
import be.zvz.kotlininside.session.user.Anonymous

class KotlinTest {
    fun initKotlinInside() {
        KotlinInside.createInstance(
            Anonymous("ㅇㅇ", "1234"),
            DefaultHttpClient(true, true)
        )
    }
    fun testArticleVote(gallId:String, articleId:Int) {
        val articleVote = ArticleVote(
            gallId = gallId,
            articleId = articleId,
            session = KotlinInside.getInstance().session
        )

        val upvoteResult = articleVote.upvote()

        println(upvoteResult)
    }
    fun main() {

        println("main function is an entry point")
    }
}