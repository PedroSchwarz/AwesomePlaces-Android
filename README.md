# AwesomePlaces-Android
App for sharing cool places around the world, written in Java for Android.

<h2>DEPENDENCIES</h2>

Circular Image View - https://github.com/hdodenhof/CircleImageView

Image Cropper - https://github.com/ArthurHub/Android-Image-Cropper

Picasso - http://square.github.io/picasso/

Firebase Firestore, Storage & auth - https://firebase.google.com/docs/android/setup

<h2>SCREENS</h2> 

<h4>Login Screen</h4>
<p>
  Here the user can access it's account or go to the registration screen to create a new one. <br>
  Thanks to @eberhardgross on Unsplash.com for the background image!
</p>
<p>
  <strong>*Uses</strong> Firebase Authentication  
</p>
<img src="images/ap_login.png" width="300" height="500">

<h4>Register Screen</h4>
<p>
  Here the user can create a new account.
</p>
<p>
  <strong>*Uses</strong> Firebase Authentication, Firebase Firestore, Firebase Storage, Circular Image View & Image Cropper
</p>
<img src="images/ap_register.png" width="300" height="500">

<h4>Main Screen</h4>
<p>
  The main screen is where the all posts are listed. <br>
  The list is filtered by the date of creation/posting. That way the newest post is always on top. <br>
  By swiping down on the list, the posts will be updated and rearranged if there are newer ones on the database. <br>
  By clicking in the fab the user has the option to post a new place.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore & Picasso
</p>
<img src="images/ap_main.png" width="300" height="500">

<h4>Add Place Screen</h4>
<p>
  Here the user can add a new place to the database. <br>
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore, Firebase Storage & Image Cropper
</p>
<img src="images/ap_add_place.png" width="300" height="500">
<img src="images/ap_categories.png" width="300" height="500">

<h4>Place Detail Screen</h4>
<p>
  When the user selects one of the posts in the main screen, he will be taken to the place's detail screen. <br>
  There will be displayed all of the informations related to the post. <br>
  On the lower card is listed the comments that other users left on the post. <br>
  By clicking the "add" icon on the right side, a dialog will slide down and enable the current user to type a comment.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore & Picasso
</p>
<img src="images/ap_post.png" width="300" height="500">
<img src="images/ap_comments.png" width="300" height="500">
<img src="images/ap_add_comments.png" width="300" height="500">
