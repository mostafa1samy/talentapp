package com.example.mostafa.talents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mostafa.talents.Models.UserModel;
import com.example.mostafa.talents.Models.post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class allposts extends AppCompatActivity {

    boolean pro = false;
    static int i = 0;
    ConstraintLayout layout;
    DatabaseReference like;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    SearchView searchView;


    postadpter userAdapter;
    List<post> userModels;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allposts);
        layout = findViewById(R.id.c);
        recyclerView = findViewById(R.id.rec);
        searchView = findViewById(R.id.searchView);


        /*
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String text=s;
                userAdapter.filter(text);
                return false;
            }
        });
        */


        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        userModels = new ArrayList<>();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        like = firebaseDatabase.getInstance().getReference();
        like.keepSynced(true);
        auth = FirebaseAuth.getInstance();

        getUsers();

    }


    private void getUsers() {
        databaseReference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModels.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    post userModel = dataSnapshot1.getValue(post.class);
                    userModels.add(userModel);
                }

                userAdapter = new postadpter(allposts.this, userModels);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.back) {
            FirebaseAuth.getInstance().signOut();

            //  onBackPressed();
            startActivity(new Intent(getApplicationContext(), StartActivity.class));
        }
        return super.onOptionsItemSelected(item);

    }

    /*
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            if (viewHolder instanceof postadpter.Viewpost) {
                // get the removed item name to display it in snack bar
                String name = userModels.get(viewHolder.getAdapterPosition()).getName();

                // backup of removed item for undo purpose
                final post deletedItem = userModels.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();

                // remove the item from recycler view
                userAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option

                Snackbar snackbar = Snackbar
                        .make(layout, name + " removed from cart!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore the deleted item
                        userAdapter.restoreItem(deletedItem, deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }

        }
    */
    class postadpter extends RecyclerView.Adapter<postadpter.Viewpost> {

        Context context;
        List<post> posts;

        public List<post> filteitem;

        public postadpter(Context context, List<post> posts) {
            this.context = context;
            this.posts = posts;
        }

        @NonNull
        @Override
        public Viewpost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.post_l, parent, false);
            return new Viewpost(view);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onBindViewHolder(@NonNull final Viewpost holder, final int position) {
            post p = posts.get(position);

            String name = p.getName();
            String desc = p.getDiscrp();
            holder.name.setText(name);
            holder.desc.setText(desc);
            String image = p.getImage();
          final  String vv=p.getVideo();
            holder.vide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, video.class);
                    intent.putExtra("mos",vv);
                    startActivity(intent);
                }
            });
          //  holder.video(video);
        //  holder.simplevideoview.setVideoURI(Uri.parse(video));


            // holder.incrment.setText(i);

            //  holder.imageView.setImageBitmap(null);
            Picasso.get().load(image).into(holder.imageView);
            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, details.class));
                }
            });
            // DatabaseReference itemRef;
            //  final Strin


            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pro = true;
                    like.child("Likes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (pro) {
                                if (dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                                    like.child("Likes").child(auth.getCurrentUser().getUid()).removeValue();
                                    //     holder.incrment.setText(++i);

                                    pro = false;


                                } else {
                                    like.child("Likes").child(auth.getCurrentUser().getUid()).setValue("likeee");
                                    //   holder.incrment.setText(i--);
                                    pro = false;

                                }
                            }
                            holder.setc();

                            //  holder.incr.setText(i);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            });

        }

        @Override
        public int getItemCount() {
            return posts.size();
        }

        public void removeItem(int position) {
            posts.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            // NOTE: don't call notifyDataSetChanged()
            notifyItemRemoved(position);
        }

        public void restoreItem(post item, int position) {
            posts.add(position, item);
            // notify item added by position
            notifyItemInserted(position);
        }

        public void filter(String chatext) {

            chatext = chatext.toLowerCase(Locale.getDefault());
            posts.clear();
            if (chatext.length() == 0) {
                posts.addAll(filteitem);
            } else {
                for (post wp : filteitem) {
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(chatext)) {
                        posts.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }


        public class Viewpost extends RecyclerView.ViewHolder {

            TextView name, desc;

            ImageView imageView, imageView2;
            ImageButton imageButton,vide;
            FirebaseAuth auth;
            DatabaseReference l;
            FirebaseDatabase database;
          //  VideoView simplevideoview;




            public Viewpost(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.tn);
              //  simplevideoview=itemView.findViewById(R.id.video);

                vide=itemView.findViewById(R.id.video);


                desc = itemView.findViewById(R.id.td);
                imageView2 = itemView.findViewById(R.id.details);
                imageView = itemView.findViewById(R.id.i);
                imageButton = itemView.findViewById(R.id.like);
                database = FirebaseDatabase.getInstance();
                l = database.getReference();
                auth = FirebaseAuth.getInstance();

            }
            /*
            public void video(String s){


                mediaController.setAnchorView(simplevideoview);
                simplevideoview.setMediaController(mediaController);
                simplevideoview.setVideoURI(Uri.parse(s));
                simplevideoview.start();
                simplevideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(context,"thank you",Toast.LENGTH_LONG).show();

                    }
                });
                simplevideoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Toast.makeText(context,"erro",Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
            }
            */

            public void setc() {
                l.child("Likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                            imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);


                        } else {
                            imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }
    }
}
