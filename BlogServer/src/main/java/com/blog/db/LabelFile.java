package com.blog.db;

import com.blog.proto.BlogStore;
import com.blog.service.File.StoreFileTree;
import org.apache.commons.lang3.StringUtils;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Table("label_file")
public class LabelFile extends Model implements CommonModel {

    public int getLabelId() {
        return getInteger("label_id");
    }

    public String getFileHash() {
        return getString("file_hash");
    }

    public static List<BlogStore.LabelFile> getAllActive(Collection<Integer> labelIdList) {
        List<LabelFile> labels = LabelFile.find("label_id in (" + StringUtils.join(labelIdList, ",") + ") AND status = ? ", BlogStore.Status.StatusActive_VALUE);
        List<BlogStore.LabelFile> list = new ArrayList<>();
        for (Map.Entry<Integer, List<LabelFile>> entry : labels.stream().collect(Collectors.groupingBy(LabelFile::getLabelId)).entrySet()) {
            list.add(BlogStore.LabelFile.newBuilder()
                    .setLabelId(entry.getKey())
                    .addAllItems(StoreFileTree.readFile(entry.getValue().stream().map(LabelFile::getFileHash).collect(Collectors.toSet())))
                    .build());
        }
        return list;
    }


    public static LabelFile saveMessage(BlogStore.LabelFile message, String fileHash, int userId) {
        LabelFile labelFile = LabelFile.findFirst("label_id = ? AND file_hash =? ", message.getLabelId(), fileHash);
        if (null == labelFile) {
            labelFile = LabelFile.create("label_id", message.getLabelId(), "file_hash", fileHash);
            labelFile.setInteger("created_by", userId);
        }
        labelFile.setInteger("label_id", message.getLabelId());
        labelFile.setInteger("updated_by", userId);
        return labelFile;
    }


}
