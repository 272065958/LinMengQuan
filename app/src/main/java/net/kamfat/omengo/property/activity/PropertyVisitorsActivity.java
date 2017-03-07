package net.kamfat.omengo.property.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.kamfat.omengo.R;
import net.kamfat.omengo.base.BaseActivity;

/**
 * Created by cjx on 2016/9/12.
 */
public class PropertyVisitorsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_visitors);

        setToolBar(true, null, R.string.property_visitor_auth);

        TextView bottomBtn = (TextView) findViewById(R.id.bottom_button);
        bottomBtn.setText(R.string.button_sure);
    }

    public void onClick(View view) {
        Intent codeIntent = new Intent(this, VisitorsCodeActivity.class);
        startActivity(codeIntent);
        finish();
    }

    public void propertySelect(View view){
        Intent intent = new Intent(this, PropertyChangeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_record:
                Intent intent = new Intent(this, VisitorsRecordActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visitors_record, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
