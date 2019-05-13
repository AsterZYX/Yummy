package aster.yummy.service;

import java.io.File;
import java.util.List;

public interface MailService {

    public void sendSimpleMail(String to, String title, String content);

    public void sendAttachmentsMail(String to, String title, String cotent, List<File> fileList);
}
