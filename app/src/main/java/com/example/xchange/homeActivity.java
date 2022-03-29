package com.example.xchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xchange.ViewHolder.ProductViewHolder;
import com.example.xchange.model.product;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestoreDataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.material.navigation.NavigationView.*;



public class homeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    final FirebaseFirestore mdb = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type = "";
    Query query= FirebaseFirestore.getInstance()
            .collection("product")
            .whereEqualTo("category","phone");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);



        //Toast.makeText(homeActivity.this,query.toString(),Toast.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                homeActivity.this, drawer, toolbar,0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView nav_user = (TextView)hView.findViewById(R.id.user_profile_name);
        CircleImageView image = (CircleImageView) hView.findViewById(R.id.user_profile_image);

        if (mAuth.getCurrentUser()!=null){
        if(mAuth.getCurrentUser().getDisplayName()!=null){
        nav_user.setText("Welcome " +mAuth.getCurrentUser().getDisplayName());}
        else{
            nav_user.setText("UserName");
        }
            if(mAuth.getCurrentUser().getPhotoUrl()!=null){

                Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl().toString()).into(image);
            }
        }
        else{
            nav_user.setText("UserName");
        }


        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(homeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        FirestoreRecyclerOptions<product> options =
                new FirestoreRecyclerOptions.Builder<product>()
                        .setQuery(query, product.class)
                        .build();

//Toast.makeText(homeActivity.this,"Option :"+options.getSnapshots(),Toast.LENGTH_LONG).show();
        FirestoreRecyclerAdapter<product, ProductViewHolder> adapter =
                new FirestoreRecyclerAdapter<product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final product model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        //Toast.makeText(homeActivity.this,"showingg..",Toast.LENGTH_LONG).show();


                        holder.itemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Admin"))
                                {
                                    Intent intent = new Intent(homeActivity.this,Edit_profile.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(homeActivity.this, productdetails.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proudct_items, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int id = item.getItemId();
//      if (id == R.id.action_settings) {
//       return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementwithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(homeActivity.this,MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.favourite) {

        } else if (id == R.id.nav_by_search) {

            Intent intent= new Intent(homeActivity.this,searchproduct.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_my_books) {

        } else if (id == R.id.nav_edit_profile) {
            if(mAuth.getCurrentUser()==null){
            item.setEnabled(false);}
            else {
                Intent intent = new Intent(homeActivity.this, Edit_profile.class);
                startActivity(intent);
                finish();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}