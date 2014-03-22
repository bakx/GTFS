package ca.synx.miway.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import ca.synx.miway.adapters.ListItemAdapter;
import ca.synx.miway.models.Route;

public class MainActivity extends Activity {

    TabHost mTabHost;
    ListView mRoutesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init tabs
        initializeTabs();

        loadRoutesTab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Load Tabs
    protected void initializeTabs() {

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        mTabHost.addTab(
                mTabHost.newTabSpec("tab_favorites")
                        .setContent(R.id.tab1)
                        .setIndicator(getResources().getString(R.string.tab_favorites)
                        )
        );

        mTabHost.addTab(
                mTabHost.newTabSpec("tab_routes")
                        .setContent(R.id.tab2)
                        .setIndicator(getResources().getString(R.string.tab_routes)
                        )
        );
    }

    protected void loadRoutesTab() {
        mRoutesListView = (ListView) findViewById(R.id.listView);

        List<Route> routes = new ArrayList<Route>();

        routes.add(new Route(1, 1, "1 Dundas", "Eastbound"));
        routes.add(new Route(2, 1, "1 Dundas", "Westbound"));

        routes.add(new Route(1, 10, "10 Bristol-Britannia", "Northbound"));
        routes.add(new Route(2, 10, "10 Bristol-Britannia", "Southbound"));

        routes.add(new Route(1, 10, "101 Dundas Express", "Eastbound"));
        routes.add(new Route(2, 10, "101 Dundas Express", "Westbound"));

        ListItemAdapter adapter = new ListItemAdapter(routes, this);
        mRoutesListView.setAdapter(adapter);
    }
}