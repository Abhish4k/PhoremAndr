package com.example.phoremandr.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.example.phoremandr.R;
import com.example.phoremandr.adapter.ContactAdapter;
import com.example.phoremandr.api_request_model.ContactListModel;
import com.example.phoremandr.base.BaseFragment;
import com.example.phoremandr.databinding.FragmentContactsBinding;
import com.example.phoremandr.utils.AppValidator;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ContactFragment extends BaseFragment {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    FragmentContactsBinding contactsBinding;

    List<ContactListModel> contactList;

    @Override
    public ViewBinding getViewModel(LayoutInflater layoutInflater, ViewGroup container) {
        contactsBinding  = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_contacts, container, false);

        contactList = new ArrayList<>();
        checkContactPermission();
        return contactsBinding;
    }



    void checkContactPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ){

            List<String> listPermissionsNeeded = new ArrayList<>();
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS);
            ActivityCompat.requestPermissions(requireActivity(),listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }else {
            if(contactList.size() > 0){
                contactList.clear();
            }

            getContactList();

            AppValidator.logData("ContactList","" + contactList.size());

            ContactAdapter adapter = new ContactAdapter(contactList);
            contactsBinding.contactListRV.setHasFixedSize(true);
            contactsBinding.contactListRV.setLayoutManager(new LinearLayoutManager(requireContext()));
            contactsBinding.contactListRV.setAdapter(adapter);

        }
    }
    @SuppressLint("Range")
    private void getContactList() {
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;

        try {
            cursor = requireContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);

        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int id = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
                int lookupKeyIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);
                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    String mCurrentLookupKey = cursor.getString(lookupKeyIndex);
                    long cursorId = cursor.getLong(id);
                    Uri selectedContactUri =
                            ContactsContract.Contacts.getLookupUri(cursorId, mCurrentLookupKey);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                        String displayName = cursor.getString(indexOfDisplayName);
                        String displayNumber = cursor.getString(indexOfDisplayNumber);


                           //Bitmap bitmap =   retrieveContactPhoto(cursorId);


                        contactList.add(new ContactListModel(displayName,null, displayNumber, selectedContactUri));
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }


    private Bitmap retrieveContactPhoto(String contactID) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(requireContext().getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);


            }


            if (inputStream != null) {
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  photo;
    }


}
