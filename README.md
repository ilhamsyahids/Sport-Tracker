# IF3210-2021-Android-K1-05

## Deskripsi aplikasi
Aplikasi yang kami buat adalah aplikasi pada platform Android yang menunjang untuk kegiatan workout. Pada aplikasi tersebut, pengguna bisa melihat berbagai berita olahraga yang terupdate, pengguna dapat melakukan tracking dari latihan yang dilakukan, melihat riwayat latihan pengguna, dan menentukan jadwal untuk latihan.

## Cara kerja, terutama mengenai pemenuhan spesifikasi aplikasi
Menu utama pada aplikasi ini adalah, Sport News, Training Tracker, Training History, dan Training Scheduler.
### Sport News
1. Pilih menu Sport News pada navigation bar yang berada di bawah.
2. Lalu, layar akan menampilkan list berita yang bisa dibaca.
3. Pilih berita olahraga yang akan dibaca.
3. Kemudian, layar akan menambilkan berita lengkap dari berita yang telah dipilih tadi.
### Training Tracker
1. Pilih menu Training Tracker pada navigation bar yang berada di bawah.
2. Pilih jenis latihan yang akan dilacak.
3. Ketuk Start untuk memulai pelacakan rute atau jumlah step.
4. Ketuk Stop untuk menghentikan pelacakan rute atau jumlah step
### Training History
1. Pilih menu Training History pada navigation bar yang berada di bawah.
2. Layar akan menampilkan riwayat latihan pengguna.
### Training Scheduler
1. Pilih menu Training Scheduler pada navigation bar yang berada di bawah.
2. Sentuh tombol "+" di pojok kanan bawah.
3. Tentukan jadwal latihan sesuai yang diinginkan.


## Library dan Asset yang digunakan dan justifikasi penggunaannya
implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation 'androidx.core:core-ktx:1.3.2'
implementation 'androidx.appcompat:appcompat:1.2.0'
implementation 'com.google.android.material:material:1.3.0'
implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
implementation 'androidx.vectordrawable:vectordrawable:1.1.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.2.0'
implementation 'androidx.legacy:legacy-support-v4:1.0.0'
testImplementation 'junit:junit:4.+'
androidTestImplementation 'androidx.test.ext:junit:1.1.1'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

// Lifecycle Service
implementation 'android.arch.lifecycle:extensions:1.1.1'

// Material Design
implementation 'com.google.android.material:material:1.3.0-alpha01'

// Room
implementation "androidx.room:room-runtime:2.2.5"
kapt "androidx.room:room-compiler:2.2.5"

// Kotlin Extensions and Coroutines support for Room
implementation "androidx.room:room-ktx:2.2.5"

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5'

// Coroutine Lifecycle Scopes
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"

// Navigation Components
implementation 'androidx.navigation:navigation-fragment-ktx:2.3.5'
implementation 'androidx.navigation:navigation-ui-ktx:2.3.5'

// Glide
implementation 'com.github.bumptech.glide:glide:4.11.0'
kapt 'com.github.bumptech.glide:compiler:4.11.0'

// Google Maps Location Services
implementation 'com.google.android.gms:play-services-location:17.0.0'
implementation 'com.google.android.gms:play-services-maps:17.0.0'

// Dagger Core
implementation "com.google.dagger:dagger:2.28.1"
kapt "com.google.dagger:dagger-compiler:2.25.2"

// Dagger Android
api 'com.google.dagger:dagger-android:2.28.1'
api 'com.google.dagger:dagger-android-support:2.28.1'
kapt 'com.google.dagger:dagger-android-processor:2.23.2'

// Activity KTX for viewModels()
implementation "androidx.activity:activity-ktx:1.1.0"

//Dagger - Hilt
implementation "com.google.dagger:hilt-android:2.28-alpha"
kapt "com.google.dagger:hilt-android-compiler:2.28-alpha"

implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
kapt "androidx.hilt:hilt-compiler:1.0.0-alpha01"

// Easy Permissions
implementation 'pub.devrel:easypermissions:3.0.0'

// Calendar view
implementation 'com.applandeo:material-calendar-view:1.7.0'

// Button Group
implementation 'com.nex3z:toggle-button-group:1.2.3'

## Screenshot aplikasi
![image](/capture/capture1.PNG)
![image](/capture/capture2.PNG)
![image](/capture/capture3.PNG)
![image](/capture/capture4.PNG)
![image](/capture/capture5.PNG)
![image](/capture/capture6.PNG)

## Pembagian kerja anggota kelompok
1. 13518028 Ilham Syahid Syamsudin  : 
2. 13518013 Raras Pradnya Pramudita : 
3. 13518046 Ferdina Wiranti Afifah  : 

