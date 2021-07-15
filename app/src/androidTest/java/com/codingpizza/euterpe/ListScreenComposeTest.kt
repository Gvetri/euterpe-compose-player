package com.codingpizza.euterpe

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import com.codingpizza.euterpe.model.ListItem
import com.codingpizza.euterpe.ui.screens.list.ItemList
import com.codingpizza.euterpe.ui.screens.list.ListState
import com.codingpizza.euterpe.ui.theme.EuterpeTheme
import org.junit.Rule
import org.junit.Test

class ListScreenComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @ExperimentalTestApi
    @Test
    fun displayItemList() {
        val itemList = (0..10).map { index ->
            ListItem(
                guid = index.toString(),
                title = "Title $index",
                description = "Description $index",
                link = "Link $index",
                publicationDate = "PublicationDate $index"
            )
        }

        composeTestRule.setContent {
            val state = ListState.SuccessFullRetrieved(itemList)
            EuterpeTheme {
                ItemList(successFullRetrieved = state) {
                    /* TODO */
                }
            }
        }

        composeTestRule.onRoot().printToLog("ListItemTest")

        val indexes = (0..10)

        indexes.forEach { index ->
            composeTestRule
                .onNode(hasScrollToIndexAction())
                .performScrollToIndex(index)

            composeTestRule
                .onNodeWithText("Title $index", useUnmergedTree = true)
                .assertExists()
        }
    }
}
