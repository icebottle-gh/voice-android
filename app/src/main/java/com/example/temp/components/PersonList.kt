package com.example.temp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun PersonList(
    props: PersonListProps,
    onFollow: (String) -> Unit,
    onUnfollow: (String) -> Unit
) {
    if (props.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // .weight(1f) // Optional: if you want it to take remaining space when list is empty
                .padding(vertical = 16.dp), // Add padding around the indicator
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp) // Example size
            )
        }
    } else {
        LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
            items(props.list.size) { index ->
                val personListItemProps = props.list[index]
                val onFollowClick = {
                    if (personListItemProps.isFollowing) {
                        onUnfollow(personListItemProps.id)
                    } else {
                        onFollow(personListItemProps.id)
                    }
                }
                PersonListItem(props = personListItemProps, onFollowClick = onFollowClick)
            }
        }
    }
}

data class PersonListProps(
    val list: List<PersonListItemProps>,
    val isLoading: Boolean,
)