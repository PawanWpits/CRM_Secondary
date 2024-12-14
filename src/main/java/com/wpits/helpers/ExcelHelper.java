package com.wpits.helpers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.entities.DeviceInventory;
import com.wpits.entities.RouterInventory;
import com.wpits.entities.SimInventory;
import com.wpits.exceptions.BadApiRequestException;
import com.wpits.exceptions.ResourceNotFoundException;

public class ExcelHelper {
	
	private static final String MSG1 = "Invalid data type at row ";
	private static final String MSG2 = ", column ";
	private static final String SHEET1 = "Sheet1";

	public static boolean checkExcelFormat(MultipartFile file) {
		
		String contentType = file.getContentType();

		if (contentType != null && contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {

			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("resource")
	public static List<SimInventory> convertExcelToListOfSimInventory(InputStream is) {

		List<SimInventory> list = new ArrayList<>();

		try {

			XSSFWorkbook workbook = new XSSFWorkbook(is);

			//XSSFSheet sheet = workbook.getSheet("data");  
			XSSFSheet sheet = workbook.getSheet(SHEET1); //sheet name should be Sheet1
			
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {
				Row row = iterator.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cells = row.iterator();

				int cellId = 0;

				SimInventory sim = new SimInventory();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cellId) {
					case 0:
						/*
						 * sim.setMsisdn((long) cell.getNumericCellValue()+""); break;
						 */
						// System.out.println("@@"+cell.getCellType());
						if (cell.getCellType() == CellType.NUMERIC) {
							//System.out.println((long) cell.getNumericCellValue()+"");
							sim.setMsisdn((long) cell.getNumericCellValue() + "");
						}
						break;
					case 1:
						if (cell.getCellType() == CellType.STRING) {
							sim.setCategory(cell.getStringCellValue());
						}
						break;

					case 2:
						if (cell.getCellType() == CellType.BOOLEAN) {
							sim.setSpecialNumber(cell.getBooleanCellValue());
						}
						break;

					case 3:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setImsi((long) cell.getNumericCellValue() + "");
						}
						break;

					case 4:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setPimsi((long) cell.getNumericCellValue() + "");
						}
						break;
					case 5:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setBatchId((int) cell.getNumericCellValue());
						}
						break;
//					case 6:
//						if (cell.getCellType() == CellType.NUMERIC) {
//							sim.setVendorId((int) cell.getNumericCellValue());
//						}
//						break;
//					case 7:
//						if (cell.getCellType() == CellType.STRING) {
//							sim.setVendorName(cell.getStringCellValue());
//						}
//						break;
//					case 8:
//						if (cell.getCellType() == CellType.NUMERIC) {
//							sim.setVendorContact((long) cell.getNumericCellValue() + "");
//						}
//						break;
//					case 9:
//						if (cell.getCellType() == CellType.STRING) {
//							sim.setVendorAddress(cell.getStringCellValue());
//						}
//						break;
					case 6:
						if (cell.getCellType() == CellType.STRING) {
							sim.setSimType(cell.getStringCellValue());
						}
						break;
					case 7:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setBuyingPriceUsd(cell.getNumericCellValue());
						}
						break;
					case 8:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setSellingPriceUsd(cell.getNumericCellValue());
						}
						break;
					case 9:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setVat(cell.getNumericCellValue() + "");
						}
						break;
					case 10:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setOtherTaxes(cell.getNumericCellValue());
						}
						break;
					case 11:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setMinCommision(cell.getNumericCellValue());
						}
						break;
					case 12:
						if (cell.getCellType() == CellType.NUMERIC) {
							sim.setMaxCommision(cell.getNumericCellValue());
						}
						break;
					case 13:
						if (cell.getCellType() == CellType.NUMERIC) {
							//System.out.println("*******"+cell.getNumericCellValue());
							sim.setAvgCommision(cell.getNumericCellValue());
						}
						break;
					default:
						break;
					}
					cellId++;
				}
				//System.out.println(sim);
				list.add(sim);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//list.stream().forEach(s->System.out.println(s.getImsi()));
		return list;
	}
	
//device
	
	@SuppressWarnings("resource")
	public static List<DeviceInventory> convertExcelToListOfDeviceInventory(InputStream is) {

		List<DeviceInventory> list = new ArrayList<>();

		try {

			XSSFWorkbook workbook = new XSSFWorkbook(is);

			XSSFSheet sheet = workbook.getSheet(SHEET1); //sheet name should be Sheet1
			
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {
				Row row = iterator.next();

				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cells = row.iterator();

				int cellId = 0;

				DeviceInventory device = new DeviceInventory();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					switch (cellId) {
					case 0:
						if (cell.getCellType() == CellType.STRING) {
							device.setDeviceModel(cell.getStringCellValue());
						}
						break;
					case 1:
						if (cell.getCellType() == CellType.STRING) {
							device.setImi(cell.getStringCellValue());
						}
						break;
					case 2:
						if (cell.getCellType() == CellType.STRING) {
							device.setDeviceMake(cell.getStringCellValue());
						}
						break;
					case 3:
						if (cell.getCellType() == CellType.STRING) {
							device.setConfiguration(cell.getStringCellValue());
						}
						break;
					case 4:
						if (cell.getCellType() == CellType.STRING) {
							device.setOstype(cell.getStringCellValue());
						}
						break;
					case 5:
						if (cell.getCellType() == CellType.STRING) {
							device.setManufacturer(cell.getStringCellValue());
						}
						break;
					case 6:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setBatchId((int) cell.getNumericCellValue());
						}
						break;
					case 7:
						if (cell.getCellType() == CellType.STRING) {
							device.setDeviceType(cell.getStringCellValue());
						}
						break;
					case 8:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setBuyingPriceUsd(cell.getNumericCellValue());
						}
						break;
					case 9:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setSellingPriceUsd(cell.getNumericCellValue());
						}
						break;
					case 10:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setVat(cell.getNumericCellValue() + "");
						}
						break;
					case 11:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setOtherTaxes(cell.getNumericCellValue());
						}
						break;
					case 12:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setMinCommision(cell.getNumericCellValue());
						}
						break;
					case 13:
						if (cell.getCellType() == CellType.NUMERIC) {
							device.setMaxCommision(cell.getNumericCellValue());
						}
						break;
					case 14:
						if (cell.getCellType() == CellType.NUMERIC) {
							//System.out.println("*******"+cell.getNumericCellValue());
							device.setAvgCommision(cell.getNumericCellValue());
						}
						break;
					default:
						break;
					}
					cellId++;
				}
				//System.out.println(sim);
				list.add(device);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//list.stream().forEach(s->System.out.println(s.getImsi()));
		return list;
	}

	//Router

	@SuppressWarnings("resource")
	public static List<RouterInventory> convertExcelToListOfRouterInventory(InputStream is) {

	    List<RouterInventory> list = new ArrayList<>();

	    try {

	        XSSFWorkbook workbook = new XSSFWorkbook(is);

	        XSSFSheet sheet = workbook.getSheet(SHEET1); // sheet name should be Sheet1

	        int rowNumber = 0;
	        
	        Iterator<Row> iterator = sheet.iterator();

	        while (iterator.hasNext()) {
	            Row row = iterator.next();

	            if (rowNumber == 0) {
	                rowNumber++;
	                continue;
	            }

	            Iterator<Cell> cells = row.iterator();

	            int cellId = 0;

	            RouterInventory router = new RouterInventory();

	            while (cells.hasNext()) {
	            	
	                Cell cell = cells.next();

	                
	                if (cell == null || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) 
	                        || (cell.getCellType() == CellType.NUMERIC && cell.getNumericCellValue() == 0.0)) {
	                	
	                    throw new ResourceNotFoundException("Cell cannot be blank at row " + rowNumber + MSG2 + cellId);
	                }

	                
	                switch (cellId) {
	                    case 0:
	                        if (cell.getCellType() == CellType.NUMERIC) {
	                            router.setSerialNumber((int) cell.getNumericCellValue());
	                        } else {
	                            throw new ResourceNotFoundException(MSG1 + rowNumber + MSG2 + cellId);
	                        }
	                        break;
	                    case 1:
	                        if (cell.getCellType() == CellType.STRING) {
	                            router.setType(cell.getStringCellValue());
	                        } else {
	                            throw new ResourceNotFoundException(MSG1 + rowNumber + MSG2 + cellId);
	                        }
	                        break;
	                    case 2:
	                        if (cell.getCellType() == CellType.STRING) {
	                            router.setBrand(cell.getStringCellValue());
	                        } else {
	                            throw new ResourceNotFoundException(MSG1 + rowNumber + MSG2 + cellId);
	                        }
	                        break;
	                    case 3:
	                        if (cell.getCellType() == CellType.STRING) {
	                            router.setCpeConfigUrl(cell.getStringCellValue());
	                        } else {
	                            throw new ResourceNotFoundException(MSG1 + rowNumber + MSG2 + cellId);
	                        }
	                        break;
	                    case 4:
	                        if (cell.getCellType() == CellType.STRING) {
	                            router.setMacAddress(cell.getStringCellValue());
	                        } else {
	                            throw new ResourceNotFoundException(MSG1 + rowNumber + MSG2 + cellId);
	                        }
	                        break;
	                    case 5:
	                        if (cell.getCellType() == CellType.STRING) {
	                            router.setDeviceManufactorer(cell.getStringCellValue());
	                        } else {
	                            throw new ResourceNotFoundException(MSG1 + rowNumber + MSG2 + cellId);
	                        }
	                        break;
	                    default:
	                        break;
	                }
	                cellId++;
	            }
	            
	            list.add(router);
	            
	            rowNumber++; 
	        }

	    } catch (ResourceNotFoundException e) {
	        throw e; 
	    } catch (Exception e) {
	        throw new BadApiRequestException(e.getMessage());
	    }
	    return list;
	}


}
