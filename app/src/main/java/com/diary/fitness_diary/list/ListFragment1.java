package com.diary.fitness_diary.list;



import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.diary.fitness_diary.DBHelper;
import com.diary.fitness_diary.list.editIntent.EditIntent;
import com.diary.fitness_diary.list.gallery.galleryIntent;
import com.diary.fitness_diary.list.intent.RoutineIntent;
import com.diary.fitness_diary.R;
import com.diary.fitness_diary.databinding.ListFragmentBinding;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class
ListFragment1 extends Fragment implements DriveAdapter.OnStarClick {
    ListFragmentBinding binding;
    RecyclerView recyclerView;
    Cursor cursorStar, cursorNormal;
    ArrayList<ItemVO> datas;
    ArrayList<ItemVO> db;
    DriveAdapter driveAdapter;
    Boolean starCheck;
    ImageView iv;
    Bitmap Abitmap;
    String mCurrentPhotoPath;
    Uri photoUri;
    Uri cam_uri;
    Bundle bundle;
    ActivityResultLauncher<Intent> startCamera;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PERMISSIONS_CODE = 123;

    private ActivityResultContract cropActivityResultContract = new ActivityResultContract<Object, Uri>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Object o) {
            return CropImage.activity()
                    .setAspectRatio(10, 13)
                    .getIntent(context);
        }

        @Nullable
        @Override
        public Uri parseResult(int i, Intent intent) {
            if (CropImage.getActivityResult(intent) == null) {
                Log.d("crop!", "뒤로가기 눌렀을때");
                return null;
            } else {
                Log.d("crop!", "제대로 실행");
                return CropImage.getActivityResult(intent).getUri();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ListFragmentBinding.inflate(inflater, container, false);
        binding.setData(this);
        recyclerView = binding.listListView;
        startAdapter();
        starCheck = false;
        iv = binding.appbarimage;


        //Uri exposure 무시
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ActivityResultLauncher<Object> cropActivityResultLauncher;

        cropActivityResultLauncher = registerForActivityResult((ActivityResultContract)
                        ListFragment1.this.cropActivityResultContract,
                (ActivityResultCallback) (new ActivityResultCallback() {
                    @Override
                    public void onActivityResult(Object result) {
                        this.onActivityResult((Uri) result);
                    }

                    private void onActivityResult(Uri result) {
                        if (result != null) {
                            iv.setImageURI(result);
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    Abitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), result));
                                } else {
                                    Abitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), result);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            setDialog();
                        }
                    }
                }));


        //카메라
        startCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //  if(result.getData()!=null) {
                        if (result.getResultCode() == -1) {
                            iv.setImageURI(cam_uri);
                            //uri to bitmap
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    Abitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), cam_uri));
                                    setDialog();
                                } else {
                                    Abitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), cam_uri);
                                    setDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "다시 시도", Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                }
        );
        setPermission();
        getGallery();

        return binding.getRoot();
    }

    //권한
    private void setPermission() {
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES};
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_PERMISSIONS_CODE);
    }

    //이미지 가져오기
    public void getGallery() {
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cGallery = db.rawQuery("select * from gallery order by galleryIndex desc limit 1", null);
        if (cGallery.getCount() == 0) {
            iv.setImageResource(R.drawable.blackman);
        } else {
            while (cGallery.moveToNext()) {
                byte[] b = cGallery.getBlob(1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                iv.setImageBitmap(bitmap);
            }
        }
    }

    //어댑터
    public void startAdapter() {
        datas = new ArrayList<>();
        db = DBSelect(datas);
        driveAdapter = new DriveAdapter(db, this);
        onSwiper(driveAdapter);     //driveAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(driveAdapter);
    }


    //DB처리
    public ArrayList DBSelect(ArrayList datas) {
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        cursorStar = db.rawQuery("select * from routine where star=?", new String[]{"true"});
        cursorNormal = db.rawQuery("select * from routine where star=?", new String[]{"false"});
        if (cursorStar.getCount() != 0) {
            HeaderItem item = new HeaderItem();
            item.headerTitle = "즐겨찾기";
            datas.add(item);
            while (cursorStar.moveToNext()) {
                DataItem dataItem = new DataItem();
                dataItem.routineName = cursorStar.getString(0);
                dataItem.star = cursorStar.getString(2);
                datas.add(dataItem);
            }

        }
        if (cursorNormal.getCount() != 0) {
            HeaderItem item = new HeaderItem();
            item.headerTitle = "목록";
            datas.add(item);
            while (cursorNormal.moveToNext()) {
                DataItem dataItem = new DataItem();
                dataItem.routineName = cursorNormal.getString(0);
                dataItem.star = cursorNormal.getString(2);
                datas.add(dataItem);
            }
        }
        db.close();
        return datas;
    }

    //db삭제
    public void dbDelete(String name) {
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from routine where routineName=?", new String[]{name});
        db.execSQL("delete from exercise where routineName=?", new String[]{name});
        db.close();
    }

    //+버튼
    public void onClickAddButton(View view) {
        Intent intent = new Intent(getActivity(), RoutineIntent.class);
        startActivityForResult(intent, 10);
    }

    //커스텀 다이얼로그
    public void setDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.list_custom_dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);//크기 조절
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //여백 없앰
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); //배경 투명
        dialog.getWindow().setGravity(Gravity.TOP); //상단 배치
        dialog.setCancelable(false); //외부 터치 금지
        dialog.show();

        Button buttonOK = dialog.findViewById(R.id.dialogOK);
        Button buttonNO = dialog.findViewById(R.id.dialogNO);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmapToJpeg(Abitmap);
                dialog.dismiss();
            }
        });
        buttonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGallery();
                dialog.dismiss();
            }
        });
    }

    //이미지 파일 저장
    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] data = stream.toByteArray();

        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("galleryName", data);
        contentValues.put("galleryDate", getDate());
        db.insert("gallery", null, contentValues);
        db.close();

    }

    //현재시간날짜 구하기
    public String getDate() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(currentTime);
    }

    //갤러리버튼
    public void galleryButton(View view) {
        Intent intent = new Intent(getActivity(), galleryIntent.class);
        startActivityForResult(intent, 10);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 102);
    }

    //카메라 버튼
    public void cameraButton(View view) {
        // 여러 권한을 배열로 정의
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            goCamera();
        }else{
            showDialog();
        }
    }

    private void showDialog(){
        AlertDialog.Builder builder = new  AlertDialog.Builder(requireContext());
        builder.setTitle("권한없음");
        builder.setMessage("권한이 필요합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                // 설정 화면으로 이동
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }


    private void goCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");


        cam_uri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);

        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY
        new Thread(new Runnable() {
            @Override
            public void run() {
                startCamera.launch(cameraIntent); // VERY NEW WAY
            }
        }).start();

    }

    //이미지 불러오기
    public void imageBring(View view) {

            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }else {
                showDialog();
            }


    }

    //intent에서 복귀 후
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 || resultCode == -1) {
            ArrayList list = new ArrayList<>();
            ArrayList<ItemVO> k = DBSelect(list);
            driveAdapter = new DriveAdapter(k, this);
            onSwiper(driveAdapter);     //driveAdapter
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(driveAdapter);
            getGallery();
        } else if (requestCode == 11) {
            ArrayList list = new ArrayList<>();
            ArrayList<ItemVO> k = DBSelect(list);
            driveAdapter = new DriveAdapter(k, this);
            onSwiper(driveAdapter);     //driveAdapter
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(driveAdapter);
            getGallery();
        }
        if (requestCode == 102) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                iv.setImageURI(selectedImageUri);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        Abitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), selectedImageUri));
                        //adjustImageAspectRatio(Abitmap);
                    } else {
                        Abitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                        //adjustImageAspectRatio(Abitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setDialog();
            }
        }
    }

    @Override
    public void onstartClick() {
        driveAdapter.notifyDataSetChanged();
    }


    //즐겨찾기와 기본 리사이클러뷰 구분
    abstract class ItemVO {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_DATA = 1;

        abstract int getType();
    }

    class HeaderItem extends ItemVO {
        String headerTitle;

        @Override
        int getType() {
            return ItemVO.TYPE_HEADER;
        }
    }

    class DataItem extends ItemVO {
        String routineName;
        String star;

        @Override
        int getType() {
            return ItemVO.TYPE_DATA;
        }
    }
    //end


    //스와이퍼
    public void onSwiper(DriveAdapter driveAdapter) {
        RecyclerView.ViewHolder viewHolder = new DriveHolder(recyclerView);
        SwipeController swipeController = new SwipeController(new SwipeController.SwipeControllerActions() {
            @Override
            public void onLeftClicked(int pos) {
                ItemVO itemVO = driveAdapter.datas.get(pos);
                DataItem dataItem = (DataItem) itemVO;
                Intent intent = new Intent(getActivity(), EditIntent.class);
                intent.putExtra("name", dataItem.routineName);
                startActivityForResult(intent, 11);

            }

            @Override
            public void onRightClicked(int pos) {
                boolean removeCheck[] = {false};
                ItemVO itemVO = driveAdapter.datas.get(pos);
                DataItem dataItem = (DataItem) itemVO;

                driveAdapter.datas.remove(pos);
                driveAdapter.notifyItemRemoved(pos);
                driveAdapter.notifyItemRangeChanged(pos, driveAdapter.datas.size() - pos);

                View CoordinatorLayout = binding.coordinatorLayout;
                Snackbar.make(CoordinatorLayout, "실행취소", Snackbar.LENGTH_LONG)
                        .setAction("실행취소", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("snack2", "onClick");
                                removeCheck[0] = true;
                                ArrayList list = new ArrayList<>();
                                ArrayList<ItemVO> k = DBSelect(list);
                                DriveAdapter newDrive = new DriveAdapter(k, (DriveAdapter.OnStarClick) getActivity());
                                onSwiper(newDrive);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(newDrive);
                            }
                        })
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                Log.d("snack2", "onDismissed");
                                if (!removeCheck[0]) {
                                    dbDelete(dataItem.routineName);
                                }
                            }
                        })
                        .show();

            }

            @Override
            public void onReset(int pos) {
                driveAdapter.notifyItemChanged(pos);
            }
        }, getResources());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //swipeController.onDraw(c);
                super.onDraw(c, parent, state);
            }
        });
    }



}

