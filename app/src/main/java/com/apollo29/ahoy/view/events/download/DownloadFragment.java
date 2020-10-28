package com.apollo29.ahoy.view.events.download;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.apollo29.ahoy.R;
import com.apollo29.ahoy.comm.event.Event;
import com.apollo29.ahoy.repository.CSVRepository;
import com.apollo29.ahoy.view.events.EventUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.rxjava3.processors.BehaviorProcessor;

import static android.app.Activity.RESULT_OK;

public abstract class DownloadFragment extends Fragment {

    protected DownloadViewModel downloadViewModel;
    private BehaviorProcessor<Integer> eventId = BehaviorProcessor.createDefault(0);

    private final static int CREATE_FILE_REQUEST_CODE = 2796;

    public void prepareCsvDownload(Event event){
        eventId.onNext(event.uid);

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.events_guests_file_name, event.title, EventUtil.getDate(event.date)));
        intent.setType("text/csv");

        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        downloadViewModel.guestlist(eventId.getValue()).observe(getViewLifecycleOwner(), guestlist -> {
            if (requestCode == CREATE_FILE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Uri FILE_PATH = data.getData();
                    if (FILE_PATH != null && eventId.getValue() > 0) {
                        try {
                            OutputStream fileStream = requireActivity().getContentResolver().openOutputStream(FILE_PATH);
                            CSVRepository.csv(fileStream, guestlist);
                        } catch (IOException e) {
                            Logger.w("Error writing File %s", e);
                        }
                    }
                }
            }
        });
    }
}
