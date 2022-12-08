package kz.talipovsn.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MySQLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 92; // НОМЕР ВЕРСИИ БАЗЫ ДАННЫХ И ТАБЛИЦ !

    static final String DATABASE_NAME = "cars"; // Имя базы данных

    static final String TABLE_NAME = "cars"; // Имя таблицы
    static final String ID = "id"; // Поле с ID
    static final String BRAND = "brand"; // Поле с наименованием организации
    static final String BRAND_LC = "name_lc"; // // Поле с наименованием организации в нижнем регистре
    static final String MODEL = "model"; // Поле с моделью
    static final String CAPACITY = "capacity"; // Поле с объемом
    static final String ACCELERATION = "acceleration"; // Поле с системой размораживания
    static final String CLASS_OF_CARS = "class_of_cars"; // Поле с классом энергопотребления
    static final String WEBSITE = "website"; // Поле с сайтом для покупки



    static final String ASSETS_FILE_NAME = "cars.txt"; // Имя файла из ресурсов с данными для БД
    static final String DATA_SEPARATOR = "|"; // Разделитель данных в файле ресурсов с телефонами

    private Context context; // Контекст приложения

    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Метод создания базы данных и таблиц в ней
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY,"
                + BRAND + " TEXT,"
                + BRAND_LC + " TEXT,"
                + MODEL + " TEXT,"
                + CAPACITY + " TEXT,"
                + ACCELERATION + " TEXT,"
                + CLASS_OF_CARS + " TEXT,"
                + WEBSITE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        System.out.println(CREATE_CONTACTS_TABLE);
        loadDataFromAsset(context, ASSETS_FILE_NAME,  db);
    }

    // Метод при обновлении структуры базы данных и/или таблиц в ней
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Добавление нового контакта в БД
    public void addData(SQLiteDatabase db, String brand, String model,
                        String capacity, String acceleration, String class_of_cars , String website) {
        ContentValues values = new ContentValues();
        values.put(BRAND, brand);
        values.put(BRAND_LC, brand.toLowerCase());
        values.put(MODEL, model);
        values.put(CAPACITY, capacity);
        values.put(ACCELERATION, acceleration);
        values.put(CLASS_OF_CARS, class_of_cars);
        values.put(WEBSITE, website);
        db.insert(TABLE_NAME, null, values);
    }


    // Добавление записей в базу данных из файла ресурсов
    public void loadDataFromAsset(Context context, String fileName, SQLiteDatabase db) {
        BufferedReader in = null;

        try {
            // Открываем поток для работы с файлом с исходными данными
            InputStream is = context.getAssets().open(fileName);
            // Открываем буфер обмена для потока работы с файлом с исходными данными
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = in.readLine()) != null) { // Читаем строку из файла
                String strTrim = str.trim(); // Убираем у строки пробелы с концов
                if (!strTrim.equals("")) { // Если строка не пустая, то
                    StringTokenizer st = new StringTokenizer(strTrim, DATA_SEPARATOR); // Нарезаем ее на части
                    String brand = st.nextToken().trim(); // Извлекаем из строки название организации без пробелов на концах
                    String model = st.nextToken().trim();
                    String capacity = st.nextToken().trim();
                    String acceleration = st.nextToken().trim();
                    String class_of_cars = st.nextToken().trim();
                    String website = st.nextToken().trim(); // Извлекаем из строки номер организации без пробелов на концах
                    addData(db, brand, model, capacity, acceleration, class_of_cars, website); // Добавляем название и телефон в базу данных
                }
            }

        // Обработчики ошибок
        } catch (IOException ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    // Получение значений данных из БД в виде строки с фильтром
    public String getData(String filter, Spinner spinner) {

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + BRAND; // Переменная для SQL-запроса
        long s = spinner.getSelectedItemId();

        if (filter.contains("'")){
            selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 0" ;
        }
        else if (s == 0 ) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + BRAND_LC + " LIKE '%" +
                    filter.toLowerCase() + "%'"
                    + " OR " + MODEL + " LIKE '%" + filter + "%'"
                    + " OR " + CAPACITY + " LIKE '%" + filter + "%'"
                    + " OR " + ACCELERATION + " LIKE '%" + filter + "%'"
                    + " OR " + CLASS_OF_CARS + " LIKE '%" + filter + "%'"
                    + " OR " + WEBSITE + " LIKE '%" + filter + "%'" + ")";
        } else if (s == 1) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + BRAND_LC + " LIKE '%" +
                    filter.toLowerCase() + "%'" + ")";
        } else if (s == 2) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + MODEL + " LIKE '%" +
                    filter.toLowerCase() + "%'" + ")";
        }else if (s == 3) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + CAPACITY + " LIKE '%" +
                    filter.toLowerCase() + "%'" + ")";
        }else if (s == 4) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + ACCELERATION + " LIKE '%" +
                    filter.toLowerCase() + "%'" + ")";
        }else if (s == 5) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + CLASS_OF_CARS + " LIKE '%" +
                    filter.toLowerCase() + "%'" + ")";
        }else if (s == 6) {
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE (" + WEBSITE + " LIKE '%" +
                    filter.toLowerCase() + "%'" + ")";
        }
        SQLiteDatabase db = this.getReadableDatabase(); // Доступ к БД
        Cursor cursor = db.rawQuery(selectQuery, null); // Выполнение SQL-запроса

        StringBuilder data = new StringBuilder(); // Переменная для формирования данных из запроса

        int num = 0;
        if (cursor.moveToFirst()) { // Если есть хоть одна запись, то
            do { // Цикл по всем записям результата запроса
                int b = cursor.getColumnIndex(BRAND);
                int m = cursor.getColumnIndex(MODEL);
                int c = cursor.getColumnIndex(CAPACITY);
                int d = cursor.getColumnIndex(ACCELERATION);
                int e = cursor.getColumnIndex(CLASS_OF_CARS);
                int w = cursor.getColumnIndex(WEBSITE);
                String brand = cursor.getString(b); // Чтение названия организации
                String model = cursor.getString(m);
                String capacity = cursor.getString(c);
                String acceleration = cursor.getString(d);
                String class_of_cars = cursor.getString(e);
                String website = cursor.getString(w); // Чтение телефонного номера
                data.append(String.valueOf(++num) + ") "+ "Бренд: " + brand + ", \n" + "Модель: " + model + ", \n" + "Объем(л): " +capacity + ", \n"
                        + "Разгон: " +acceleration + ", \n" + "Класс машины: "+ class_of_cars + ", \n" + "Купить: " + website + "\n");
            } while (cursor.moveToNext()); // Цикл пока есть следующая запись
        }
        return data.toString(); // Возвращение результата
    }

}