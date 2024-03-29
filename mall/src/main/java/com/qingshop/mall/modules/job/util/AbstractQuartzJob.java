package com.qingshop.mall.modules.job.util;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingshop.mall.common.constant.ScheduleConstants;
import com.qingshop.mall.common.utils.ExceptionUtil;
import com.qingshop.mall.common.utils.StringUtils;
import com.qingshop.mall.common.utils.bean.BeanUtils;
import com.qingshop.mall.common.utils.spring.SpringUtils;
import com.qingshop.mall.modules.job.entity.SysJob;
import com.qingshop.mall.modules.job.entity.SysJobLog;
import com.qingshop.mall.modules.job.service.ISysJobLogService;

/**
 * 抽象quartz调用
 */
public abstract class AbstractQuartzJob implements org.quartz.Job {
	private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

	/**
	 * 线程本地变量
	 */
	private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SysJob job = new SysJob();
		BeanUtils.copyBeanProp(job, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
		try {
			before(context, job);
			if (job != null) {
				doExecute(context, job);
			}
			after(context, job, null);
		} catch (Exception e) {
			log.error("Mission error  - ：", e);
			after(context, job, e);
		}
	}

	/**
	 * 执行前
	 *
	 * @param context
	 *            工作执行上下文对象
	 * @param sysJob
	 *            系统计划任务
	 */
	protected void before(JobExecutionContext context, SysJob job) {
		threadLocal.set(new Date());
	}

	/**
	 * 执行后
	 *
	 * @param context
	 *            工作执行上下文对象
	 * @param sysScheduleJob
	 *            系统计划任务
	 */
	protected void after(JobExecutionContext context, SysJob job, Exception e) {
		Date startTime = threadLocal.get();
		threadLocal.remove();

		final SysJobLog jobLog = new SysJobLog();
		jobLog.setJobName(job.getJobName());
		jobLog.setJobGroup(job.getJobGroup());
		jobLog.setInvokeTarget(job.getInvokeTarget());
		jobLog.setStartTime(startTime);
		jobLog.setEndTime(new Date());
		long runMs = jobLog.getEndTime().getTime() - jobLog.getStartTime().getTime();
		jobLog.setJobMessage(jobLog.getJobName() + " Used Time：" + runMs + " ms");
		if (e != null) {
			jobLog.setStatus("1");
			String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
			jobLog.setExceptionInfo(errorMsg);
		} else {
			jobLog.setStatus("0");
		}

		// 写入数据库当中
		SpringUtils.getBean(ISysJobLogService.class).addJobLog(jobLog);
	}

	/**
	 * 执行方法，由子类重载
	 *
	 * @param context
	 *            工作执行上下文对象
	 * @param job
	 *            系统计划任务
	 * @throws Exception
	 *             执行过程中的异常
	 */
	protected abstract void doExecute(JobExecutionContext context, SysJob job) throws Exception;
}