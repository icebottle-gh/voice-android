package org.noormahal.vp25.android.presentation.ui

//import org.noormahal.vp25.android.data.StoryList
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.noormahal.vp25.android.R
import org.noormahal.vp25.android.data.User
import org.noormahal.vp25.android.presentation.navigation.Screen
import org.noormahal.vp25.android.presentation.viewmodel.MainViewModel
import org.noormahal.vp25.android.presentation.viewmodel.StoriesViewModel
import org.noormahal.vp25.android.theme.VpTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun StoriesList(navController: NavHostController, mainViewModel: MainViewModel, storiesViewModel : StoriesViewModel) {
//    val usersList = storiesViewModel.usersList.collectAsState(initial = listOf())

    val usersList = MutableStateFlow(listOf(
        User("Saji", "Sajidha Abdulla", true),
        User("sali", "Muhammed Salih", true),
        User("hahi", "Ravi", true),
        User("kiki", "Ahmed", false),
        User("chuchu", "Dani", false),
    ))


    // also get your stories
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding()){

        //Your Story card here
        item {
            StoryItem(user = User("123", "Sajidha", hasUnviewedStory = false), isMyStory = true){
                //navigate to my stories detail
            }
        }

        //other users stories card
        items(usersList.value){
                user->
            StoryItem(user = user) {
                navController.navigate(Screen.StoriesDetail.createRoute(user.userName))
            }
        }
    }

}
//    val usersList = storiesViewModel.getStoryListUsers


@Composable
fun StoryItem(user: User, isMyStory:Boolean=false, onClick:()->Unit){
    var cardTitle=
        if (isMyStory){
            if (user.hasUnviewedStory)
                "My Story"
            else
                "Add Story"
        }
        else
            user.displayName

    val borderColor =  if (isMyStory || user.hasUnviewedStory)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick()
            }
            .then(
                if (isMyStory && !user.hasUnviewedStory) {
                    Modifier.drawBehind {
                        val strokeWidth = 3.dp.toPx()
                        val dashLength = 10f
                        val gapLength = 10f
                        val pathEffect =
                            PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), 0f)
                        drawRoundRect(
                            color = borderColor, // Use the resolved color
                            style = Stroke(width = strokeWidth, pathEffect = pathEffect),
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                } else {
                    Modifier.border(
                        width = 1.5.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = borderColor // Use the resolved color
                    )
                }
            ),
//            .border(
//                width = 1.5.dp,
//                shape = RoundedCornerShape(24.dp),
//                color = borderColor
//            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Display user Profile Image
//                if (user.userImageThumb != null) {
//                    // Load profile image from URL or local resource
//                    Image(
//                        painter = rememberAsyncImagePainter(user.userImageThumb), // Use Coil/Glide if loading from URL
//                        contentDescription = "Profile Image",
//                        modifier = Modifier
//                            .size(50.dp)
//                            .clip(CircleShape)
//                    )
//                } else {
                    // Use default profile icon
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "Default Profile Picture",
//                    //TODO:color explicit here, change to be theme friendly
////                        tint = colorResource(id = R.color.grey_300),
//                    modifier = Modifier
//                        .size(50.dp)
////                            .fillMaxSize()
//                )
//                }
                Spacer(modifier = Modifier.width(8.dp))
                //username
                Text(
                    text = cardTitle,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
//                Spacer(modifier = Modifier.weight(1f))
                //TODO:color explicit here, change to be theme friendly
//                Text(text = "Just Now",
//                    fontSize = 12.sp,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                ) //color = colorResource(id = R.color.grey_A700))
                if (isMyStory)
                    IconButton(
                        onClick = {
                            //TODO NAVIGATE TO ADD NEW STORY
                        },
                        modifier = Modifier
                            .size(22.dp)
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_post_add_24),
                            contentDescription = "Add Story",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
            }
        }
    }
}


@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StoryItemPreview(){
    VpTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                StoryItem(User("234", "Sajidha Abdulla", true), true, {})
                StoryItem(User("234", "Muhammed Salih", true), false, {})
                // Viewed story -> outlineVariant ring
                StoryItem(User("234", "John Doe", false), false, {})
            }
        }
    }
}