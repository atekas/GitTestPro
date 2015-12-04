package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.SpellOrderAdapter;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class SpellOrderActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListView;
    private SpellOrderAdapter mSpellOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spell_order_activity);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.group_buy_selection).setOnClickListener(this);
        findViewById(R.id.my_group_buy).setOnClickListener(this);
        findViewById(R.id.group_buy_selection_text).setSelected(true);

        mListView = (ListView) findViewById(R.id.group_buy_list);
        mSpellOrderAdapter = new SpellOrderAdapter(this);
        mListView.setAdapter(mSpellOrderAdapter);
        mListView.setOnItemClickListener(this);
        findViewById(R.id.input_word).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.group_buy_selection:
                findViewById(R.id.group_buy_selection_text).setSelected(true);
                findViewById(R.id.my_group_buy_text).setSelected(false);
                findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
                break;
            case R.id.my_group_buy:
                findViewById(R.id.group_buy_selection_text).setSelected(false);
                findViewById(R.id.my_group_buy_text).setSelected(true);
                findViewById(R.id.bottom_layout).setVisibility(View.GONE);
                break;
            case R.id.input_word:
                commandInput(findViewById(R.id.input_word));
                break;
        }
    }

    /**
     *
     * 输入口令
     *
     */
    Dialog mCommandInputDialog;
    public void commandInput(View v){
        mCommandInputDialog = new Dialog(this,R.style.dialog);
        mCommandInputDialog.setCancelable(true);
        mCommandInputDialog.setContentView(R.layout.command_dialog);
        mCommandInputDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(this, SpellOrderDetailsActivity.class));
    }
}
