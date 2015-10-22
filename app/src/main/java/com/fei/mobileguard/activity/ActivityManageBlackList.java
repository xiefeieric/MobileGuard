package com.fei.mobileguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fei.mobileguard.R;
import com.fei.mobileguard.db.dao.BlackListDao;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ActivityManageBlackList extends Activity {

    private ListView lvBlackList;
    private List<String> mList;
    private MyListAdapter mAdapter;
    private BlackListDao mBlacklist;
    private EditText etPage;
    private TextView tvPage;

    private int currentPage = 0;
    private int pageSize = 20;
    private int offset = 20;
    private int mTotalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_black_list);
        mBlacklist = new BlackListDao(this);
        mList = new ArrayList<>();
//        initData();
        initViews();
        initListeners();
    }

    private void initListeners() {

//        mList = mBlacklist.queryAll();
        mList = mBlacklist.queryPerPage(pageSize,offset*currentPage);
        mAdapter = new MyListAdapter();
        lvBlackList.setAdapter(mAdapter);
    }

    public void addBlackNumber(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Number");
        final AlertDialog dialog = builder.create();
        View addView = View.inflate(this, R.layout.dialog_add_black_number, null);
        final EditText mEtAddBlackNumber = (EditText) addView.findViewById(R.id.etAddBlackNumber);

        Button mBtnAddBlackNumberOk = (Button) addView.findViewById(R.id.btnAddBlackNumberOk);
        mBtnAddBlackNumberOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mEtAddBlackNumber.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    mBlacklist.addData(number);
                    if (mAdapter!=null) {
                        mList.add(number);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter = new MyListAdapter();
                        lvBlackList.setAdapter(mAdapter);
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(ActivityManageBlackList.this,"Please enter number...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button mBtnAddBlackNumberCancel = (Button) addView.findViewById(R.id.btnAddBlackNumberCancel);
        mBtnAddBlackNumberCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(addView);
        dialog.show();
    }

    private void initViews() {
        lvBlackList = (ListView) findViewById(R.id.lvBlackList);
        etPage = (EditText) findViewById(R.id.etPage);
        tvPage = (TextView) findViewById(R.id.tvPage);
        mTotalPage = mBlacklist.getCount()/pageSize;
//        System.out.println(totalCount);
        tvPage.setText(currentPage+"/"+ mTotalPage);
    }

    public void prePage(View view) {
        if (currentPage==0) {
            Toast.makeText(this,"This is the first page.",Toast.LENGTH_SHORT).show();
        } else {
            currentPage--;
            initListeners();
            tvPage.setText(currentPage+"/"+ mTotalPage);
        }
    }

    public void nextPage(View view) {
        if (currentPage==mTotalPage) {
            Toast.makeText(this,"This is the last page.",Toast.LENGTH_SHORT).show();
        } else  {
            currentPage++;
            initListeners();
            tvPage.setText(currentPage+"/"+ mTotalPage);
        }
    }

    public void jumpTo(View view) {
        String page = etPage.getText().toString();
        if (!TextUtils.isEmpty(page)) {

            if (Integer.parseInt(page)>=0 && Integer.parseInt(page)<=mTotalPage) {
                currentPage = Integer.parseInt(page);
                initListeners();
                tvPage.setText(currentPage+"/"+ mTotalPage);
                etPage.setText("");
            } else {
                Toast.makeText(this,"Wrong page number!",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Can not be null!",Toast.LENGTH_SHORT).show();
        }

    }

//    private void initData() {
//        BlackListDao blackListDao = new BlackListDao(this);
//        for (int i=0;i<100;i++) {
//            blackListDao.addData("1355310000"+i);
//        }
//    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            View view = null;
            ViewHolder holder;
            if (convertView==null) {
                holder = new ViewHolder();
                convertView  = View.inflate(ActivityManageBlackList.this,R.layout.list_view_item_call,null);
                holder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumberBlack);
                holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.tvNumber.setText(mList.get(position));

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlackListDao database = new BlackListDao(ActivityManageBlackList.this);
                    database.deleteData(mList.get(position));
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvNumber;
        ImageView ivDelete;
    }
}
