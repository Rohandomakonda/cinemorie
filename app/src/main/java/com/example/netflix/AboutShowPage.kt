package com.example.netflix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter


@Composable
fun ShowDetailScreen(
    data: showDetails
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = data.backgroundImage,
            contentDescription = "${data.title} Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)

        ){
            Surface(
                modifier = Modifier
                    .width(500.dp)
                    .height(300.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(50,41,47).copy(0.95f), // semi-transparent
                tonalElevation = 6.dp, // subtle shadow
                shadowElevation = 8.dp
            ){
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = data.title,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(30.dp, 16.dp)
                            )
                            Text(
                                text = data.tags,
                                fontSize = 15.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(end = 30.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,  // Border thickness
                                        color = Color.White,  // Border color
                                        shape = CircleShape  // Ensure the border follows the circle shape
                                    )
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 4.dp
                                    )  // Adjust the padding to make sure text is inside the circle
                            )
                        }
                        Spacer(modifier = Modifier.height(0.dp))
                        Text(
                            text = data.genres.joinToString(),
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier.padding(30.dp, 2.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(16.dp)  // Optional: set icon size
                            )
                            Spacer(modifier = Modifier.width(2.dp))  // Optional spacing between icon and text
                            Text(
                                text = "${data.rating} ${data.year} ${data.ageRating}",
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                        Text(
                            text = data.description,
                            fontSize = 15.sp,
                            color = Color.White,
                            modifier = Modifier.padding(start = 30.dp).width(300.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!data.episodes.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Row {
                                Text(
                                    text = "Episodes",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 30.dp)
                                )

                            }
                            Column(modifier = Modifier.padding(30.dp)) {
                                data.episodes.forEach { (epName, epData) ->
                                    val (epDesc, epImageUrl) = epData
                                    val painter = rememberAsyncImagePainter(R.drawable.billy)

                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Box(modifier = Modifier.clickable{}
                                        , contentAlignment = Alignment.Center) {
                                            Image(
                                                painter = painter,
                                                contentDescription = "$epName image",
                                                modifier = Modifier
                                                    .height(150.dp)
                                                    .width(150.dp)
                                                    .clip(RoundedCornerShape(16.dp))
                                                    .background(Color.Gray)
                                                    .border(
                                                        2.dp,
                                                        Color.White,
                                                        RoundedCornerShape(16.dp)
                                                    ),
                                                contentScale = ContentScale.Crop
                                            )
                                            Box(modifier = Modifier.background(color = Color.Gray.copy(0.6f),CircleShape)) {
                                                Icon(
                                                    imageVector = Icons.Default.PlayArrow,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .border(
                                                            border = BorderStroke(
                                                                2.dp,
                                                                Color.Black
                                                            ), shape = CircleShape
                                                        )
                                                        .size(50.dp),
                                                    tint = Color.Black
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = epName,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = epDesc,
                                                fontSize = 14.sp,
                                                color = Color.LightGray,
                                                maxLines = 4, // or Int.MAX_VALUE if you want full text
                                                overflow = TextOverflow.Ellipsis // optional: shows "..." if cut
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        if (!data.morelikethis.isNullOrEmpty()) {
                            Column(modifier = Modifier.padding(30.dp, 8.dp)) {
                                Text(
                                    text = "More Like this: ",
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding()
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow() {
                                    items(data.morelikethis) {(name,image)->
                                        val imagePainter = rememberAsyncImagePainter(image)
                                        Column(modifier = Modifier.padding(end=8.dp).width(105.dp)){
                                            Image(
                                                painter = imagePainter,
                                                contentDescription = "${name} picture",
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .clip(RoundedCornerShape(20))
                                                    .background(Color.Gray, RoundedCornerShape(20))
                                                    .border(2.dp, Color.White, RoundedCornerShape(20)),
                                                contentScale = ContentScale.Crop
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = name,
                                                fontSize = 15.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}




