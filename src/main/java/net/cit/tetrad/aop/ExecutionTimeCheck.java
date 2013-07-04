/**
*    Copyright (C) 2012 Cardinal Info.Tech.Co.,Ltd.
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License, version 3,
*    as published by the Free Software Foundation.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.cit.tetrad.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class ExecutionTimeCheck {
	
	private static Logger logger = Logger.getLogger(ExecutionTimeCheck.class);
	
	public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
		String signatureString = joinPoint.getSignature().toShortString();
		logger.debug(signatureString + " start!!");
		long start = System.currentTimeMillis();
		try {
			Object result = joinPoint.proceed();
			return result;
		} finally {
			long finish = System.currentTimeMillis();
			logger.debug(signatureString + " end!!");
			logger.info(signatureString + " execution time : " + (finish - start) + "ms");
		}
	}
}
