package developer.citypalestine8936ps.new_more_feature.books

data class BookData(
    val bookType: String = "",
    val books: List<InnerBook> = listOf()
)

data class InnerBook(
    val bookName: String = "",
    val bookPhoto: String = "",
    val bookUrl: String = ""
)
