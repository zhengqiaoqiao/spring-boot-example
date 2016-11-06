package org.example.server.ftp.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import net.sf.oval.constraint.NotEmpty;

import org.apache.commons.lang3.StringUtils;
import org.example.api.base.Result;
import org.example.api.enums.FtpConf;
import org.example.api.enums.ResultCodeEnum;
import org.example.common.image.ImageUtils;
import org.example.server.ftp.service.IFtpService;
import org.example.server.system.log.LoggerCable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;




@RestController
@RequestMapping("/upload-ftp")
public class FtpController {
	private final Logger logger = LoggerFactory.getLogger(FtpController.class);
	@Resource
	private IFtpService ftpService;
	
	@RequestMapping("/image")
    public Result<Boolean> uploadImage(@RequestParam @NotEmpty MultipartFile file, @RequestParam String fileName) throws IOException {
		InputStream in = null;
		try {
			//判断图片大小
			if(file.getSize() > FtpConf.IMAGE.getMaxSize()){
				logger.error("图片超出最大上传限制（{}）", FtpConf.IMAGE.getMaxSize());
			}
			in = file.getInputStream();
			if(StringUtils.isEmpty(fileName)){
				fileName = file.getName();
			}
			boolean b = ftpService.uploadFile(in, FtpConf.IMAGE.getPath(), fileName);
			if(b){
				return Result.create(ResultCodeEnum.SUCCESS.getKey(), "图片上传成功！", b);
			}else{
				return Result.create(ResultCodeEnum.WARN.getKey(), "图片上传失败！", b);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return Result.create(ResultCodeEnum.FAILURE.getKey(), "程序发生错误:" + e.getMessage(), false);
		}

    }
	
	@RequestMapping("/image-base64")
    public Result<Boolean> uploadBase64Image(@RequestParam @NotEmpty String base64Str, @RequestParam String fileName) throws IOException {
		InputStream in = null;
		try {
			String[] arr = base64Str.split(",");
			if (arr.length == 2) {
				in = ImageUtils.base64Decoder(arr[1]);
			}
			if(StringUtils.isEmpty(fileName)){
				fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
			}
			boolean b = ftpService.uploadFile(in, FtpConf.IMAGE.getPath(), fileName);
			if(b){
				return Result.create(ResultCodeEnum.SUCCESS.getKey(), "图片上传成功！", b);
			}else{
				return Result.create(ResultCodeEnum.WARN.getKey(), "图片上传失败！", b);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return Result.create(ResultCodeEnum.FAILURE.getKey(), "程序发生错误:" + e.getMessage(), false);
		}

    }

}
