package li.demon.com.tvrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import li.demon.com.tvrecyclerview.widget.TVGridLayoutManager;
import li.demon.com.tvrecyclerview.widget.TVLinearLayoutManager;
import li.demon.com.tvrecyclerview.widget.TVRecyclerView;

public class MainActivity extends AppCompatActivity {


    TVRecyclerView tvRecyclerView;
    TextView serialNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRecyclerView = (TVRecyclerView)findViewById(R.id.recycler_view);
        serialNum = (TextView) findViewById(R.id.serial_num);


        InnerAdapter adapter = new InnerAdapter(this);
        TVGridLayoutManager layoutManager = new TVGridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false);
        layoutManager.setTVRecyclerView(tvRecyclerView);
        layoutManager.showFocusViewNextItem(true);
        layoutManager.setShowNextItemScale(0.5f);
        tvRecyclerView.setLayoutManager(layoutManager);
        tvRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    class InnerAdapter extends RecyclerView.Adapter{


        private Context context ;
        private LayoutInflater mInflater;
        public InnerAdapter(Context context) {
            this.context = context;
            this.mInflater = LayoutInflater.from(this.context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = mInflater.inflate(R.layout.rv_item,parent,false);
            RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view){};
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            RecyclerView.LayoutParams rvItemParam = (RecyclerView.LayoutParams)holder.itemView.getLayoutParams();
            if(rvItemParam!=null){
                rvItemParam.width = ScreenUtils.getScreenWidth(context)/3;
                rvItemParam.topMargin = 20;
                rvItemParam.bottomMargin = 20;
                rvItemParam.leftMargin = 20;
                rvItemParam.rightMargin = 20;
            }


            TextView textView = (TextView) holder.itemView.findViewById(R.id.serial_num);

            textView.setText("当前序号："+position);

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        v.setBackgroundColor(Color.CYAN);
                    }else{
                        v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}
