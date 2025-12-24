package ma.emsi.ebank.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Intercepte toutes les méthodes dans le package services (et sous-packages)
    @Around("execution(* ma.emsi.ebank.services..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        String method = joinPoint.getSignature().toShortString();

        log.info("➡️  START {}", method);

        try {
            Object result = joinPoint.proceed(); // laisse la méthode s’exécuter normalement
            log.info("✅ END   {}", method);
            return result;
        } catch (Throwable ex) {
            log.error("❌ ERROR {} : {}", method, ex.getMessage());
            throw ex; // on relance l'exception (donc aucun changement métier)
        }
    }
}
