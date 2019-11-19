package org.highwire.alert.sync.controller;

import org.highwire.alert.sync.AlertSyncPoller;

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
@RequestMapping
public class PollerController {

  @Autowired
  private TaskScheduler taskScheduler;

  @Resource
  private AlertSyncPoller poller;

  @RequestMapping(value = "/poller/start", method = RequestMethod.GET)
  public String start(final RedirectAttributes redirectAttributes) {

    log.debug("startPoller() : {}");

    poller.enablePolling();

    redirectAttributes.addFlashAttribute("css", "success");
    redirectAttributes.addFlashAttribute("msg", "Poller is Started.");

    return "redirect:/poller/mgmt";

  }

  /**
   * To Stop the Poller
   *
   * @return .
   */
  @RequestMapping(value = "/poller/stop", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<String> stopPoller() {
    log.debug("stopPoller() : {}");
    try {
      poller.disablePolling();
      HttpHeaders headers = new HttpHeaders();
      headers.setCacheControl("no-cache");
      return new ResponseEntity<String>("SUCCESS", headers, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Validation Error :: " + e.getMessage());
      return new ResponseEntity<String>("FAILURE", HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/poller/mgmt", method = RequestMethod.GET)
  public String showPollerManagement(Model model) {

    log.debug("showPollerManagement");
    String status = poller.isPollingEnabled() ? "Running" : "Stopped";

    model.addAttribute("status", status);

    return "poller/mgmt";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String listAllPolicy(Model model) {
    return "redirect:/poller/mgmt";
  }
}
