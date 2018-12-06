package cz.vutbr.fit.meetmeal.engine

import androidx.databinding.ObservableField
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.Exception
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnCompleteListener
import android.content.SharedPreferences
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.AuthCredential



class UserEngine {

  private val collection = "user"

  private var db = FirebaseFirestore.getInstance()
  private var auth = FirebaseAuth.getInstance()

  fun loginUser(email: String, password: String)
    : Completable = Completable.create { singleSubscriber ->
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
      if (task.isSuccessful) {
        singleSubscriber.onComplete()
      } else {
        singleSubscriber.onError(task.exception ?: Exception())
      }
    }
  }

  fun logoutUser() {
    auth.signOut()
  }

  fun registerUser(email: String, password: String, user: User)
    : Completable = Completable.create { singleSubscriber ->
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
      val firebaseUser = auth.currentUser
      if (task.isSuccessful && firebaseUser != null) {
        db.collection(collection)
          .document(firebaseUser.uid)
          .set(user).addOnCompleteListener{ task2 ->
            if (task2.isSuccessful) {
              singleSubscriber.onComplete()
            } else {
              singleSubscriber.onError(task2.exception ?: Exception())
            }
          }
      } else {
        singleSubscriber.onError(task.exception ?: Exception())
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
        user?.id = result.id
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

  fun changePass(pass: String){
    getCurrentFirebaseUser()?.updatePassword(pass)
  }

  fun checkCurrentPass(password: String){
    val email = getCurrentFirebaseUser()?.email.toString()

    val credential = EmailAuthProvider.getCredential(email, password)

    // Prompt the user to re-provide their sign-in credentials
    getCurrentFirebaseUser()?.reauthenticate(credential)?.addOnCompleteListener { task ->
              if (task.isSuccessful) {
                task.isSuccessful
              } else {
                // Password is incorrect
                task.isCanceled
              }
            }
  }

}