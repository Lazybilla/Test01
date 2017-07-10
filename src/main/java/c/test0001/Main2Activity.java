package c.test0001;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity {

    private Button mChangeImage ;
    private Button mChangeStatus ;
    private CircleImageView mProfilePicture ;
    private TextView mStatus ;
    private TextView mDisplayStatus;

    // Database References
    private DatabaseReference mUserNameReference;
    private FirebaseAuth mAuth ;

    // Database Storage
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

          String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
          mProfilePicture = (CircleImageView)findViewById(R.id.profile_image);

          mChangeStatus = (Button)findViewById(R.id.ChangeStatus);
          mChangeImage = (Button)findViewById(R.id.changephotoButton);
          mStatus = (TextView)findViewById(R.id.UserStatus);
          mDisplayStatus = (TextView)findViewById(R.id.UserName);
          mStorageReference = FirebaseStorage.getInstance().getReference();

          Toast.makeText(Main2Activity.this,user,Toast.LENGTH_LONG).show();

          mUserNameReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user);

          mUserNameReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                 //    String name = (String) dataSnapshot.child("username").getValue();

                //  String status = (String) dataSnapshot.child("status").getValue();

                  String image = (String) dataSnapshot.getValue();


                //  mStatus.setText(status);

                // mDisplayStatus.setText(name);

               // Toast.makeText(Main2Activity.this,dataSnapshot.toString(),Toast.LENGTH_SHORT).show();

              Picasso.with(getApplicationContext()).load(image).into(mProfilePicture);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

              String image = (String) dataSnapshot.getValue();

                // Toast.makeText(Main2Activity.this,dataSnapshot.toString(),Toast.LENGTH_SHORT).show();

                     Picasso.with(Main2Activity.this).load(image).into(mProfilePicture);



            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


         mChangeImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 // Cropper First

                 CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(Main2Activity.this);


             }
         });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

             if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)

             {
                 CropImage.ActivityResult result = CropImage.getActivityResult(data);


                 if (resultCode == RESULT_OK) {
                     Uri uri = result.getUri() ;

                     String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

                   final  StorageReference file = mStorageReference.child("profilePictures").child(user+".jpg") ;
                     file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                             if(task.isSuccessful())
                             {
                                 String downloadurl = task.getResult().getDownloadUrl().toString();


                                 mUserNameReference.child("image").setValue(downloadurl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful())
                                         {
                                             Toast.makeText(Main2Activity.this,"It works",Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 });

                             // Toast.makeText(Main2Activity.this,"It Fucking works",  Toast.LENGTH_SHORT).show();
                             }
                             else
                             {
                                 Toast.makeText(Main2Activity.this,"It doesnt work",Toast.LENGTH_SHORT).show();
                             }


                         }
                     });


                  //   CropImage.activity().setAspectRatio(1,1);

                  //   Toast.makeText(Main2Activity.this,uri.toString() ,Toast.LENGTH_SHORT).show();

                 }
                 else
                 {
                     Exception error = result.getError();
                 }

             }

    }
}
