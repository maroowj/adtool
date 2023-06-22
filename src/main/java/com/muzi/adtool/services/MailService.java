package com.muzi.adtool.services;

import com.muzi.adtool.dao.MemberDAO;
import com.muzi.adtool.domain.CertificationVO;
import com.muzi.adtool.domain.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MailService{


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberDAO memberDAO;

    @Autowired
    CertificationService certificationService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Resource (name = "uploadPath")
    String path;


    @Transactional
    public int sendMail(String member_id) throws MessagingException {
        int result = 0;
        MemberVO memberVO = memberDAO.findByMemberId(member_id);

        if(memberVO==null) {
            result=0;
        }else if(memberVO.getWithdrawal()==1) {
            result=2; // 탈퇴한 ID
        }else {
            result=1;

            String certification_code=generatedRandomKey();
            CertificationVO certificationVO = new CertificationVO();

            certificationVO.setMember_seq(memberVO.getMember_seq());
            certificationVO.setMember_id(memberVO.getMember_id());
            certificationVO.setCertification_code(certification_code);

            certificationService.insertCertification(certificationVO);

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true, "UTF-8");

            StringBuilder content = new StringBuilder();

            content.append("<h1>─── RESET YOUR PASSWORD ───</h1><br/>");
            content.append("<p>Thank you for contact adtool customer service.</p>");
            content.append("<p>We sent the link to reset your password.</p>");
            content.append("<p>This link will be expired in a week and if you reset your password.</p>");
            content.append("<p>Click on the logo below to reset your password.</p>");
            content.append("<a href='http://adtool.kr/reset/"+certification_code +"'><img src='cid:logo'></a><br/><br/>"); // 배포 시 꼭 배포 되는 url 확인
            content.append("<p>※ This email will not be answered. Don't reply.</p>");
            content.append("<hr/>");

            mimeMessageHelper.setFrom(new InternetAddress("cs@adtool.com")); // 발신자
            mimeMessageHelper.setSubject("RESET YOUR PASSWORD");
            mimeMessageHelper.setText(content.toString(), true);

            FileSystemResource logo = new FileSystemResource(path +"icon/logo.png");

            mimeMessageHelper.addInline("logo", logo);

            mimeMessageHelper.setTo(member_id);

            javaMailSender.send(msg);
        }

        return result;
    }



    private static String generatedRandomKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
