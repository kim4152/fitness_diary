package com.diary.fitness_diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=27;
    public DBHelper(@Nullable Context context) {
        super(context, "fitnessDB",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String routine="CREATE TABLE routine (\n" +
               "\t\"routineName\"\tTEXT NOT NULL,\n" +
               "\t\"color\"\tTEXT DEFAULT 'purple_200',\n" +
               "\t\"star\"\tTEXT DEFAULT 'false',\n" +
               "\tPRIMARY KEY(\"routineName\")\n" +
               ");";
       String exercise="CREATE TABLE \"exercise\" (\n" +
               "\t\"exerciseName\"\tTEXT,\n" +
               "\t\"routineName\"\tTEXT,\n" +
               "\tFOREIGN KEY(\"routineName\") REFERENCES routine(\"routineName\") \n" +
               ");";
       String details ="CREATE TABLE \"details\" (\n" +
               "\t\"dateRoutineExerciseSet\"\tTEXT,\n" +
               "\t\"set1\"\tTEXT,\n" +
               "\t\"weight\"\tTEXT,\n" +
               "\t\"time\"\tTEXT,\n" +
               "\t\"routineName\"\tTEXT,\n" +
               "\t\"date\"\tTEXT,\n" +
               "\t\"realRoutineName\"\tTEXT,\n" +
               "\tPRIMARY KEY(\"dateRoutineExerciseSet\")\n" +
               ");";
       String gallery ="create table gallery("+
               "galleryIndex integer primary key autoincrement,"+
               "galleryName BLOB,"+
               "galleryDate text);";
        String exColor ="create table exColor("+
                "exName text primary key);";


       db.execSQL(routine);
       db.execSQL(exercise);
       db.execSQL(details);
       db.execSQL(gallery);
       db.execSQL(exColor);

        //db.execSQL("insert into routine (routineName,color) values ('가슴','#55ff0000')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
//            db.execSQL("drop table routine");
//            db.execSQL("drop table exercise");
//            db.execSQL("drop table details");
//            db.execSQL("drop table gallery");
//            db.execSQL("drop table exColor");
//            onCreate(db);
        }
    }
}
