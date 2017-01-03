package li.demon.com.tvrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import li.demon.com.tvrecyclerview.utils.ScaleUitl;
import li.demon.com.tvrecyclerview.utils.ScreenUtils;
import li.demon.com.tvrecyclerview.widget.TVListFocusBorder;
import li.demon.com.tvrecyclerview.widget.TVMoveLayoutManager;
import li.demon.com.tvrecyclerview.widget.TVRecyclerView;

public class MainActivity extends AppCompatActivity {


    TVRecyclerView tvRecyclerView;
    TextView serialNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRecyclerView = (TVRecyclerView) findViewById(R.id.recycler_view);
        serialNum = (TextView) findViewById(R.id.serial_num);

        InnerAdapter adapter = new InnerAdapter(this);
        TVMoveLayoutManager layoutManager = new TVMoveLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setTVRecyclerView(tvRecyclerView);
        layoutManager.setStartOffset(200);

        TVListFocusBorder listFocusBorder = new TVListFocusBorder(this,tvRecyclerView);
        listFocusBorder.bind();
        tvRecyclerView.setLayoutManager(layoutManager);
        tvRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }


    class InnerAdapter extends RecyclerView.Adapter {


        private Context context;
        private LayoutInflater mInflater;

        public InnerAdapter(Context context) {
            this.context = context;
            this.mInflater = LayoutInflater.from(this.context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = mInflater.inflate(R.layout.rv_item, parent, false);
            RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
            };
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            RecyclerView.LayoutParams rvItemParam = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (rvItemParam != null) {
                rvItemParam.width = ScreenUtils.getScreenWidth(context) / 3;
                rvItemParam.topMargin = 30;
                rvItemParam.bottomMargin = 30;
                rvItemParam.leftMargin = 30;
                rvItemParam.rightMargin = 30;
            }


            TextView textView = (TextView) holder.itemView.findViewById(R.id.serial_num);

            textView.setText("当前序号：" + position);

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        v.setBackgroundColor(Color.CYAN);
                        ScaleUitl.scaleViewWidthAnim(v,1.04f,200);
                        tvRecyclerView.onListItemFocused(v,position,1.04f);

                    } else {
                        ScaleUitl.scaleViewWidthAnim(v,1.0f,200);
                        tvRecyclerView.onListItemUnFocused(v,position,1.0f);
                        v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    }
                }
            });
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
}
