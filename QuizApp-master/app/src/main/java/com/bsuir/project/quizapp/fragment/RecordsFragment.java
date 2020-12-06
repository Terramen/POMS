package com.bsuir.project.quizapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsuir.project.quizapp.FirebaseInstance;
import com.bsuir.project.quizapp.R;
import com.bsuir.project.quizapp.adapter.RecordRecyclerViewAdapter;
import com.bsuir.project.quizapp.entity.Record;

import java.util.ArrayList;
import java.util.List;

//import static com.bsuir.german.quizapp.activity.MainActivity.db;


public class RecordsFragment extends Fragment {

//    private DAORecord daoRecord = new DAORecord(db);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_records, container, false);

        List<Record> recordList = new ArrayList<>();
        recordList = fillRecordList(recordList);

        // Inflate the layout for this fragment
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        RecordRecyclerViewAdapter adapter = new RecordRecyclerViewAdapter(recordList, v.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        return v;
    }

    private List<Record> fillRecordList(List<Record> recordList) {
        recordList = new FirebaseInstance().getRecords();
        Log.e("TAG", "fillRecordList: " + recordList.size() );
        //daoRecord.selectAllRecordsOrderedByScore();
        return recordList;
    }
}
