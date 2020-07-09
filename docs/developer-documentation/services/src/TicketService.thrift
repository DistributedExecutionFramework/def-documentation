include "CommunicationDTOs.thrift"

namespace java at.enfilo.def.communication.api.ticket.thrift
namespace py def_api.thrift.ticket

typedef string TicketID

/**
* Ticket Status Resource Interface
*/
service TicketService {

    /**
    * Request status of the ticket by its id.
    */
    CommunicationDTOs.TicketStatusDTO getTicketStatus(1: TicketID ticketId);

    /**
    * Request ticket cancelation by its id. Returns ticket status 'CANCELED' on successfull cancelation.
    */
    CommunicationDTOs.TicketStatusDTO cancelTicketExecution(1: TicketID ticketId, 2: bool mayInterruptIfRunning);
}


