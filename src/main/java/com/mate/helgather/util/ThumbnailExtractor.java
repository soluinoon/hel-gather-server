package com.mate.helgather.util;

import com.mate.helgather.exception.DefaultImageException;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ThumbnailExtractor {
    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/static/images/default-thumbnail.png";
    private static final String THUMBNAIL_EXTENSION = "png";

    public File extractThumbnail(File source, String path) throws Exception {
        // 썸네일 파일 생성
        File thumbnail = new File(getLocalHomeDirectory(), path);

        try {
            FrameGrab frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(source));
            // 첫 프레임의 데이터
            frameGrab.seekToSecondPrecise(0);
            Picture picture = frameGrab.getNativeFrame();
            // 썸네일 파일에 복사
            BufferedImage bi = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bi, THUMBNAIL_EXTENSION, thumbnail);

            return thumbnail;
        } catch (Exception e) {
            try {
                // 실패했을 경우에 기본 이미지를 사용
                e.printStackTrace();
                return new File(DEFAULT_IMAGE_PATH);
            } catch (Exception ex) {
                throw new DefaultImageException();
            }
        }
    }

    public String getLocalHomeDirectory() {
        // 로컬
//        return System.getProperty("user.home");
        // 서버
        return "/home/ubuntu/";
    }
}
