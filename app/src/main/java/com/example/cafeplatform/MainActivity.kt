package com.example.cafeplatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafeplatform.ui.theme.CafeplatformTheme
import androidx.compose.runtime.*
import com.example.cafeplatform.ui.auth.LoginActivity
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.cafeplatform.ui.admin.AdminLoginActivity
import com.google.firebase.firestore.FirebaseFirestore
import coil.compose.AsyncImage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeplatformTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HomeScreen()
                }
            }
        }
    }
}

// Data class
data class Cafe(
    val nama: String = "",
    val alamat: String = "",
    val foto: String = "", // nanti di-load dari Firebase Storage
    val link_gmaps: String = "",
    val rating: Double = 0.0,
    val owner_uid: String = ""
)




// Main UI
@Composable
fun HomeScreen() {
    val db = FirebaseFirestore.getInstance()
    var cafes by remember { mutableStateOf(listOf<Cafe>()) }

    LaunchedEffect(Unit) {
        db.collection("cafe")
            .get()
            .addOnSuccessListener { result ->
                val data = result.map { it.toObject(Cafe::class.java) }
                cafes = data
            }
    }

    LazyColumn {
        items(cafes) { cafe ->
            CafeCard(cafe)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }




    @Composable
    fun SearchBar() {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text("Cari..", color = Color(0xFF834D1E))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFFCF2D9),
                focusedContainerColor = Color(0xFFFCF2D9),
                unfocusedBorderColor = Color(0xFF834D1E),
                focusedBorderColor = Color(0xFF834D1E),
                cursorColor = Color(0xFF834D1E),
                focusedTextColor = Color(0xFF834D1E),
                unfocusedTextColor = Color(0xFF834D1E)
            )
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8E3B6))
            .padding(16.dp)
            .padding(top = 45.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = { },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color(0xFF834D1E)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFCF2D9),
                contentColor = Color(0xFF834D1E)
            )
        ) {
            Text("Daftar Kafe Di Bandung")
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            items(cafes) { cafe ->
                CafeCard(cafe)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Selamat Datang Di Cafe-X",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF834D1E)
        )
        Row {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifikasi",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF834D1E)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { showSheet = true },
                tint = Color(0xFF834D1E)
            )
        }
    }

    if (showSheet) {
        val context = LocalContext.current
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        showSheet = false
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCF2D9),
                        contentColor = Color(0xFF834D1E)
                    )
                ) {
                    Text("Login")
                }


                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        showSheet = false
                        context.startActivity(Intent(context, AdminLoginActivity::class.java))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFCF2D9),
                        contentColor = Color(0xFF834D1E)
                    )
                ) {
                    Text("Login Admin")
                }
            }
        }
    }
}


@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Cari..") },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun CafeCard(cafe: Cafe) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFCF2D9)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cafe.foto,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cafe.nama, fontWeight = FontWeight.Bold, color = Color(0xFF834D1E))
                Text(text = cafe.alamat, fontSize = 12.sp, color = Color(0xFF834D1E))
                Text(text = "Rating: ${cafe.rating}", fontWeight = FontWeight.Medium, color = Color(0xFF834D1E))
            }

        }
    }
}

