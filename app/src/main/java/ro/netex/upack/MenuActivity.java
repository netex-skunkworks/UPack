package ro.netex.upack;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends MapActivity implements NavigationView.OnNavigationItemSelectedListener  {

    NavigationView navigationView;
    Menu menu;
    DrawerLayout drawer;
    Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public void setNavigationToolbar(NavigationView navigation, Menu navigationMenu, Toolbar toolbar, DrawerLayout drawer, int itemId) {
        this.drawer = drawer;
        this.navigationView = navigation;
        this.menu = navigationMenu;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(itemId).setChecked(true);
        menu = navigationView.getMenu();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.supplier:
                Intent intent = new Intent(context, PackageActivity.class);
                context.startActivity(intent);
                super.test();
                Log.i("Item", String.valueOf(id));
                break;
            case R.id.pedding_list:
                Log.i("Item", String.valueOf(id));
                break;
            case R.id.delivery:
                Log.i("Item", String.valueOf(id));
                break;
            case R.id.account:
                Log.i("Item", String.valueOf(id));
                break;
            case R.id.login:
                Log.i("Item", String.valueOf(id));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
