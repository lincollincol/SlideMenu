package linc.com.slidemenu

import java.lang.Exception

class ContainerNotFoundException : Exception(
    "SlideContainerView not found! SlideMenuLayout should contains SlideContainerView!"
) {
}