package com.cqtest.stateequationssolver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

public class SubstanceDb extends SimpleCursorAdapter {
    static final String[] from = new String[] {"name"};
    static final int[] to  = new int[]{android.R.id.text1};

    public SubstanceDb(Context context) {
        super(context, android.R.layout.simple_spinner_item, null, from, to);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        changeCursor(substanceCursor());
    }

    private Cursor substanceCursor()
    {
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("data2.db");

        return db.query("substance", new String[]{"name","_id"}, null, null, null, null, null, null);
    }

    public double[] getSubstanceParam(long _id)
    {
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("data2.db");

        Cursor c = db.query("substance", new String[]{"Tc","Pc","w","Zc","Vc"}, "_id="+_id, null, null, null, null, null);
        if(c.moveToFirst()){
            double params[] = new double[c.getColumnCount()];
            for (int i=0; i<params.length; i++)
                params[i] = c.getDouble(i);
            return params;
        }
        c.close();
        return null;
    }
}
