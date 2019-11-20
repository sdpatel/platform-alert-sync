package org.highwire.alert.sync.controller;

import org.highwire.alert.sync.AlertSyncPoller;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Slf4j
@Controller
@RequestMapping("/poller")
public class PollerController {

  @Autowired
  private TaskScheduler taskScheduler;

  @Resource
  private AlertSyncPoller poller;

  @RequestMapping(value = "/start", method = RequestMethod.GET, produces = {
      "application/json"})
  public @ResponseBody ResponseEntity<String> start(final RedirectAttributes redirectAttributes) {
    JsonObject resp = new JsonObject();
    HttpStatus respStatus = HttpStatus.OK;
    log.info("startPoller called ");
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl("no-cache");
    try {
      poller.enablePolling();
      resp.addProperty("poller-status", poller.isPollingEnabled());
      resp.addProperty("status", HttpStatus.OK.value());
    } catch (Exception e) {
      log.error(" Error Enabling Poller :: " + e.getMessage());
      respStatus = HttpStatus.BAD_REQUEST;
      resp.addProperty("status", HttpStatus.BAD_REQUEST.value());
      resp.addProperty("error", " Error Enabling Poller :: " + e.getMessage());
    }
    return new ResponseEntity<String>(resp.toString(), headers, respStatus);
//    redirectAttributes.addFlashAttribute("css", "success");
//    redirectAttributes.addFlashAttribute("msg", "Poller is Started.");
//    return resp.toString();

  }

  /**
   * To Stop the Poller
   *
   * @return .
   */
  @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody ResponseEntity<String> stopPoller() {
    JsonObject resp = new JsonObject();
    HttpStatus respStatus = HttpStatus.OK;
    log.info("stopPoller() called ");
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl("no-cache");
    try {
      poller.disablePolling();
      resp.addProperty("status", HttpStatus.OK.value());
    } catch (Exception e) {
      log.error(" Error Disabling Poller :: " + e.getMessage());
      respStatus = HttpStatus.BAD_REQUEST;
      resp.addProperty("status", HttpStatus.BAD_REQUEST.value());
      resp.addProperty("error", " Error Disabling Poller :: " + e.getMessage());
    }
    String status = poller.isPollingEnabled() ? "Running" : "Stopped";
    resp.addProperty("poller-status", status);
    return new ResponseEntity<String>(resp.toString(), headers, respStatus);
  }

  @RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody ResponseEntity<String> showPollerStatus() {
    JsonObject resp = new JsonObject();
    HttpStatus respStatus = HttpStatus.OK;
    String status = "Failed";

    log.info("Show Poller Status ");
    try {
      status = poller.isPollingEnabled() ? "Running" : "Stopped";
      resp.addProperty("poller-status", status);
      resp.addProperty("status", HttpStatus.OK.value());
    } catch (Exception e) {
      log.error(" Error Disabling Poller :: " + e.getMessage());
      respStatus = HttpStatus.BAD_REQUEST;
      resp.addProperty("status", HttpStatus.BAD_REQUEST.value());
      resp.addProperty("error", " Error Checking Poller Status :: " + e.getMessage());
    }
    return new ResponseEntity<String>(resp.toString(), respStatus);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String defaultToStatus(Model model) {
    return "redirect:/poller/status";
  }
}
