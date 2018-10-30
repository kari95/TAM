package cz.vutbr.fit.meetmeal.engine

import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.*
import java.lang.Exception

class UserEngine {

  private val collection = "user"

  private var db = FirebaseFirestore.getInstance()
  private var auth = FirebaseAuth.getInstance()

  fun loginUser(email: String, password: String)
    : Observable<Boolean> = Observable.create { singleSubscriber ->
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
      singleSubscriber.onNext(task.isSuccessful)
    }
  }

  fun logoutUser() {
    auth.signOut()
  }

  fun registerUser(email: String, password: String, user: User)
    : Observable<Boolean> = Observable.create { singleSubscriber ->
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
      val firebaseUser = auth.currentUser
      if (task.isSuccessful && firebaseUser != null) {
        db.collection(collection)
          .document(firebaseUser.uid)
          .set(user).addOnCompleteListener{ task ->
            singleSubscriber.onNext(task.isSuccessful)
          }
      } else {
        singleSubscriber.onNext(false)
      }
    }
  }

  fun getCurrentUser() : Observable<User> {
    val currentUserId = auth.currentUser?.uid ?: "none"
    return findUser(currentUserId)
  }

  fun getCurrentFirebaseUser() : FirebaseUser? = auth.currentUser

  fun findUser(id: String)
    : Observable<User> = Observable.create { singleSubscriber ->
    var docRef = db.collection(collection).document(id)
    docRef.get().addOnCompleteListener { task ->
      val result = task.result
      if (task.isSuccessful && result != null) {
        val user = result.toObject(User::class.java)
        if (user != null) {
          singleSubscriber.onNext(user)
        } else {
          singleSubscriber.onError(Exception("User not found"))
        }
      } else {
        singleSubscriber.onError(Exception("User not found"))
      }
    }
  }
}