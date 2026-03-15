package com.hospitalcrud.dao.repositories.xmlFiles;

import com.hospitalcrud.config.Configuration;
import com.hospitalcrud.dao.model.MedRecord;
import com.hospitalcrud.dao.model.MedRecordListWrapper;
import com.hospitalcrud.dao.repositories.MedRecordRepository;
import com.hospitalcrud.utils.Constantes;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Repository
@Profile("txtFiles")
public class XmlMedRecordRepository implements MedRecordRepository {

    private List<MedRecord> loadAllMedRecords() throws JAXBException {
        File file = new File(Configuration.getInstance().getProperty(Constantes.MED_RECORD_PATH));

        if (!file.exists())
            return new ArrayList<>();

        JAXBContext context = JAXBContext.newInstance(MedRecordListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        MedRecordListWrapper wrapper = (MedRecordListWrapper) unmarshaller.unmarshal(file);
        return wrapper.getMedRecords();
    }

    private void saveAllMedRecords(List<MedRecord> medRecords) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(MedRecordListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        MedRecordListWrapper wrapper = new MedRecordListWrapper();
        wrapper.setMedRecords(medRecords);
        marshaller.marshal(wrapper, new File(Configuration.getInstance().getProperty(Constantes.MED_RECORD_PATH)));
    }

    @Override
    public List<MedRecord> getAll(int idPatient) {
        try {
            List<MedRecord> allRecords = loadAllMedRecords();
            return allRecords.stream()
                    .filter(medRecord -> medRecord.getIdPatient() == idPatient)
                    .toList();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public int add(MedRecord medRecord) {
        try {
            List<MedRecord> allRecords = loadAllMedRecords();
            medRecord.setId(getLastId());
            allRecords.add(medRecord);
            saveAllMedRecords(allRecords);
            updateNextId(getLastId() + 1);
            return 1;
        } catch (JAXBException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void updateNextId(int nextId) {
        Configuration.getInstance().setProperty(Constantes.MED_RECORD_NEXT_ID, String.valueOf(nextId));
    }

    private int getLastId() {
        return Integer.parseInt(Configuration.getInstance().getProperty(Constantes.MED_RECORD_NEXT_ID));
    }

    @Override
    public void delete(int id) {
        try {
            List<MedRecord> allRecords = loadAllMedRecords();
            allRecords.removeIf(medRecord -> medRecord.getId() == id);
            saveAllMedRecords(allRecords);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(MedRecord medRecord) {
        try {
            List<MedRecord> allRecords = loadAllMedRecords();
            for (int i = 0; i < allRecords.size(); i++) {
                if (allRecords.get(i).getId() == medRecord.getId()) {
                    allRecords.set(i, medRecord);
                    break;
                }
            }
            saveAllMedRecords(allRecords);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
