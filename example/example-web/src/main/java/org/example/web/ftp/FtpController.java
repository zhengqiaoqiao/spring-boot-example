package org.example.web.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.example.api.base.Result;
import org.example.api.enums.FtpConf;
import org.example.api.enums.ResultCodeEnum;
import org.example.api.ftp.service.IFtpServiceApi;
import org.example.common.convert.ConvertUtil;
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
	private IFtpServiceApi ftpServiceApi;
	
	@RequestMapping("/image")
    public Result<Boolean> uploadImage(@RequestParam MultipartFile file, @RequestParam String fileName) throws IOException {
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
			boolean b = ftpServiceApi.uploadFile(in, FtpConf.IMAGE.getPath(), fileName);
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
    public Result<Boolean> uploadBase64Image(@RequestParam String base64Str, @RequestParam String fileName) throws IOException {
		InputStream in = null;
		try {
			String[] arr = base64Str.split(",");
			if (arr.length == 2) {
				in = ConvertUtil.base64Decoder(arr[1]);
			}
			if(StringUtils.isEmpty(fileName)){
				fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
			}
			boolean b = ftpServiceApi.uploadFile(in, FtpConf.IMAGE.getPath(), fileName);
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
	
	
	/**
	 * 上传文件分片
	 * @param fileSplit
	 * @param fileMD5Str
	 * @param fileSplitNo
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/file-split")
    public Result<Boolean> uploadFileSplit(@RequestParam MultipartFile file, @RequestParam String fileMD5, @RequestParam int chunk) throws IOException {
		InputStream in = null;
		try {
			//判断图片大小
			if(file.getSize() > FtpConf.IMAGE.getMaxSize()){
				logger.error("图片超出最大上传限制（{}）", FtpConf.IMAGE.getMaxSize());
			}
			in = file.getInputStream();
			if(StringUtils.isEmpty(fileMD5)){
				logger.error("上传文件的MD5为空！请传入MD5值，以便校验！");
			}
			boolean b = ftpServiceApi.uploadFileSplit(in, FtpConf.IMAGE.getPath(), fileMD5, chunk);
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
	/**
	 * 合并文件分片
	 * @param fileMD5
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/merge-file-split")
    public Result<Boolean> mergeFileSplit(@RequestParam String fileMD5, @RequestParam String fileName) throws IOException {
		try {
			boolean b = ftpServiceApi.mergeFileSplit(FtpConf.IMAGE.getPath(), fileMD5, fileName);
			if(b){
				return Result.create(ResultCodeEnum.SUCCESS.getKey(), "合并分片成功！", b);
			}else{
				return Result.create(ResultCodeEnum.WARN.getKey(), "合并分片失败！", b);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return Result.create(ResultCodeEnum.FAILURE.getKey(), "程序发生错误:" + e.getMessage(), false);
		}
    }
	/**
	 * 判断远程文件是否存在
	 * @param fileMD5
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/check-file-exist")
    public Result<Boolean> checkFileExist(@RequestParam String fileName) throws IOException {
		try {
			boolean b = ftpServiceApi.checkFileExist(FtpConf.IMAGE.getPath(), fileName);
			return Result.create(ResultCodeEnum.SUCCESS.getKey(), "远程文件存在！", b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			return Result.create(ResultCodeEnum.FAILURE.getKey(), "程序发生错误:" + e.getMessage(), false);
		}
    }

}
