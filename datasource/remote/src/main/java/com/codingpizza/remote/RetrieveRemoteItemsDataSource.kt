package com.codingpizza.remote

import arrow.core.Either
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.open.RetrieveItemsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrieveRemoteItemsDataSource @Inject constructor() : RetrieveItemsDataSource {

    private val listItem1 = ListItem(
        guid = "1",
        title = "Big Buck Bunny",
        link = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        description = "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
        publicationDate = ""
    )
    private val listItem2 = ListItem(
        guid = "2",
        title = "Elephant Dream",
        link = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        description = "The first Blender Open Movie from 2006",
        publicationDate = ""
    )
    private val listItem3 = ListItem(
        guid = "3",
        title = "For Bigger Blazes",
        link = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        description = "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes. For \$35.\\nLearn how to use Chromecast with HBO GO and more at google.com/chromecast.",
        publicationDate = ""
    )
    private val listItem4 = ListItem(
        guid = "4",
        title = "Sintel",
        link = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        description = "Sintel is an independently produced short film, initiated by the Blender Foundation as a means to further improve and validate the free/open source 3D creation suite Blender. With initial funding provided by 1000s of donations via the internet community, it has again proven to be a viable development model for both open 3D technology as for independent animation film.\\nThis 15 minute film has been realized in the studio of the Amsterdam Blender Institute, by an international team of artists and developers. In addition to that, several crucial technical and creative targets have been realized online, by developers and artists and teams all over the world.\\nwww.sintel.org",
        publicationDate = ""
    )

    private val defaultListItem = listOf(
        listItem2,
        listItem1,
        listItem3,
        listItem4
    )

    override suspend fun retrieveItems(): List<ListItem> = defaultListItem

    override fun observeItems(): Flow<List<ListItem>> {
        return flow {
            emit(defaultListItem)
        }
    }

    override suspend fun retrieveItemsByQuery(query: String): Either<Error, List<ListItem>> {
        val result = defaultListItem.filter { listItem -> listItem.title.contains(query) }
        return Either.Right(result)
    }

    override suspend fun retrieveItemById(id: String): Either<Unit, ListItem> {
        val result = defaultListItem.find { it.guid == id }
        return Either.fromNullable(result)
    }
}
