
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfest.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context,
    private val name: MutableList<String>,
    private val price: MutableList<String>,
    private val cartDescription: MutableList<String>,
    private val image: MutableList<String>,
    private var cartQuantity: MutableList<Int>,
    private var   cartIngredient : MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    //        Made by Muhammad Noman
//
//        If you need my service or you have any question then you can contact with me.
//
//        WhatsApp number :  +923104881573
//
//        LinkedIn account :  https://www.linkedin.com/in/muhammad-noman59
//
//        Facebook account :  https://www.facebook.com/profile.php?id=100092720556743&mibextid=ZbWKwL



    private val auth = FirebaseAuth.getInstance()

    init {

        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemsNumber = name.size
        itemQuantits = IntArray(cartItemsNumber) { 1 }
        cartRef = database.reference.child("Users").child(userId).child("Cart Items")
    }

    companion object {

        private var itemQuantits: IntArray = intArrayOf()
        private lateinit var cartRef: DatabaseReference
    }

//    private val itemQuantitys = IntArray(name.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(position)

    }

    override fun getItemCount(): Int = name.size

    // Get updated quantities
    fun getUpdatedItemQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }


    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                val itemQuantity = itemQuantits[position]
                cartFoodName.text = name[position]
                cartPrice.text = price[position]
                carItemQuantity.text = itemQuantity.toString()

                //load image using Glide
                val uriString = image[position]
//                Log.d("image", "food Url: $uriString")
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)
                minusBtn.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusBtn.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteBtn.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }


            }

        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantits[position] < 10) {
                itemQuantits[position]++
                cartQuantity[position] = itemQuantits[position]
                binding.carItemQuantity.text = itemQuantits[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantits[position] > 1) {
                itemQuantits[position]--
                cartQuantity[position] = itemQuantits[position]
                binding.carItemQuantity.text = itemQuantits[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItems(position,uniqueKey)
                }
            }
        }

        private fun removeItems(position: Int, uniqueKey: String) {
            if (position < 0 || position >= name.size) {
                Log.e("CartAdapter", "Invalid position: $position")
                return
            }

            if (uniqueKey == null) {
                Log.e("CartAdapter", "Unique key is null")
                return
            }

            cartRef.child(uniqueKey).removeValue().addOnSuccessListener {
                if (position >= 0 && position < name.size) {
                    name.removeAt(position)
                    image.removeAt(position)
                    price.removeAt(position)
                    cartDescription.removeAt(position)

                    // Check if position is within cartQuantity bounds
                    if (position >= 0 && position < cartQuantity.size) {
                        cartQuantity.removeAt(position)
                    } else {
                        Log.e("CartAdapter", "Invalid position for cartQuantity: $position")
                    }

                    // Check if position is within cartIngredient bounds
                    if (position >= 0 && position < cartIngredient.size) {
                        cartIngredient.removeAt(position)
                    } else {
                        Log.e("CartAdapter", "Invalid position for cartIngredient: $position")
                    }

                    // Update UI after removing item
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, name.size)

                    Toast.makeText(context, "Ürün sepetten kaldırıldı.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("CartAdapter", "Invalid position: $position")
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }


        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {

            cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }

                    onComplete(uniqueKey)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }


    }


}