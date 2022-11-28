import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
//import com.example.android_sns_45.databinding.ActivityMyinfoBinding
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore

public class MyinfoActivity: AppCompatActivity() {
    //var firestore: FirebaseFirestore? = null
    var fireauth:FirebaseAuth? = null

    var myuid: String? = null
    var frienduid: String? = null

    private val imageView = ImageView(parent)

    override fun onCreate(savedInstanceState: Bundle?) {
        myuid = FirebaseAuth.getInstance().currentUser?.uid
      //  firestore = FirebaseFirestore.getInstance()
        fireauth = FirebaseAuth.getInstance()



        super.onCreate(savedInstanceState)
        //val binding = ActivityMyinfoBinding.inflate(layoutInflater)
        //setContentView(binding.root)


        // val adapter = MyinfoAdapter(imageView)
        //binding.postimageRecyclerView.adapter = adapter
        // binding.postimageRecyclerView.layoutManager = GridLayoutManager(this, 3)

    }



}